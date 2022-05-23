package cn.codeonce.controller;

import cn.codeonce.service.OrderDetailService;
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
@RestController
@RequestMapping
public class OrderDetailController {

    @Autowired
    private OrderDetailService service;

}
