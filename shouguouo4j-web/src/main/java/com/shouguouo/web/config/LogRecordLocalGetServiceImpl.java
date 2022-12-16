package com.shouguouo.web.config;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.IOperationLogGetService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2022-12-14 20:56:41
 */
@Component
@Slf4j
public class LogRecordLocalGetServiceImpl implements IOperationLogGetService {

    @Override
    public boolean createLog(LogDTO logDTO) throws Exception {
        log.info("logDTO: [{}]", JSON.toJSONString(logDTO));
        return true;
    }
}
