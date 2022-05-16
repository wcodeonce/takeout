package cn.codeonce.service;

import cn.codeonce.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.core.annotation.Order;

/**
 * codeonce
 * OrderService
 * Yunle_TakeOut
 * 2022/5/15
 */
public interface OrderService extends IService<Orders> {

    // 用户下单
    public void submit(Orders orders);
}
