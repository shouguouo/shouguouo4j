package com.shouguouo.web.configcenter;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 基于AsyncContext的异步长轮询
 *
 * @author shouguouo
 * @date 2022-08-17 11:08:10
 */
@SpringBootApplication
@RestController
@Slf4j
public class ConfigServer {

    private final Multimap<String, AsyncTask> dataIdContext = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    private final ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setNameFormat("longPolling-timeout-checker-%d").build());

    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class, args);
    }

    /**
     * 长轮询入口
     * 客户端发起监听 curl -X GET "localhost:8081/listener?dataId=1"
     */
    @RequestMapping("/listener")
    public void addListener(HttpServletRequest request, HttpServletResponse response) {

        String dataId = request.getParameter("dataId");

        // 开启异步
        AsyncContext asyncContext = request.startAsync(request, response);
        AsyncTask asyncTask = new AsyncTask(asyncContext, true);

        // 维护 dataId 和异步请求上下文的关联
        dataIdContext.put(dataId, asyncTask);

        // 启动定时器，30s 后写入 304 响应
        timeoutChecker.schedule(() -> {
            if (asyncTask.isTimeout()) {
                // 配置未发布过，则返回304
                dataIdContext.remove(dataId, asyncTask);
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                asyncContext.complete();
            }
        }, 30, TimeUnit.SECONDS);
    }

    /**
     * 发布配置：curl -X GET "localhost:8081/publishConfig?dataId=1&configInfo=hello"
     */
    @RequestMapping("/publishConfig")
    @SneakyThrows
    public String publishConfig(String dataId, String configInfo) {
        log.info("publish configInfo dataId: [{}], configInfo: {}", dataId, configInfo);
        Collection<AsyncTask> asyncTasks = dataIdContext.removeAll(dataId);
        for (AsyncTask asyncTask : asyncTasks) {
            // 配置发布后，写入响应
            asyncTask.setTimeout(false);
            HttpServletResponse response = (HttpServletResponse) asyncTask.getAsyncContext().getResponse();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(configInfo);
            asyncTask.getAsyncContext().complete();
        }
        return "success";
    }

    @Data
    @AllArgsConstructor
    private static class AsyncTask {

        // 长轮询请求上下文 包含请求和响应体
        private AsyncContext asyncContext;

        private boolean timeout;
    }
}
