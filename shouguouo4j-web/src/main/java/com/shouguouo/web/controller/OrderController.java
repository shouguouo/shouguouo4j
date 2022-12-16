package com.shouguouo.web.controller;

import cn.monitor4all.logRecord.annotation.OperationLog;
import com.shouguouo.web.vo.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shouguouo
 * @date 2022-12-14 20:44:09
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping("/change")
    @OperationLog(bizType = "'orderChange'", bizId = "#order.id", msg = "'修改标题为：' + #order.title")
    public boolean change(@RequestBody Order order) {
        // TODO
        return true;
    }
}
