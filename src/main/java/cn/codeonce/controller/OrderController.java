package cn.codeonce.controller;

import cn.codeonce.common.R;
import cn.codeonce.pojo.Orders;
import cn.codeonce.service.OrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * codeonce
 * OrderController
 * TakeOut
 * 2022/5/15
 */
@Slf4j
@Api(tags = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService service;


    /**
     * 用户下单
     *
     * @param orders 订单信息
     * @return 操作响应
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("用户下单数据=>{}", orders);

        // 执行订单提交方法
        this.submit(orders);

        return R.success("下单成功!");
    }

}
