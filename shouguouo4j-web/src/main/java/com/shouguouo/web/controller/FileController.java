package com.shouguouo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @author shouguouo
 * @date 2023-12-17 11:59:04
 */
@RestController
@RequestMapping("file")
public class FileController {

    @GetMapping("download")
    public void download(@RequestParam("path") String path, HttpServletResponse response) throws Exception {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        OutputStream os = response.getOutputStream();

        // 设置响应头信息
        response.setContentType("binary/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        response.setContentLengthLong(fis.getChannel().size());
        // Etag、Last-Modified
        // 创建缓冲区
        byte[] buffer = new byte[4096];
        int bytesRead;

        // 逐块读取文件内容并写入到响应流中
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        // 关闭流
        fis.close();
        os.flush();
        os.close();
    }
}
