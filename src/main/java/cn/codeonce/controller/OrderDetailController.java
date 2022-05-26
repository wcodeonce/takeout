package cn.codeonce.controller;

import cn.codeonce.service.OrderDetailService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * codeonce
 * OrderDetailController
 * TakeOut
 * 2022/5/15
 */
@Slf4j
@Api(tags = "订单详情接口")
@RestController
@RequestMapping
public class OrderDetailController {

    @Autowired
    private OrderDetailService service;

}
