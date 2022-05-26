package cn.codeonce.service;

import cn.codeonce.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * codeonce
 * OrderService
 * TakeOut
 * 2022/5/15
 */
public interface OrderService extends IService<Orders> {

    // 用户下单
    void submit(Orders orders);
}
