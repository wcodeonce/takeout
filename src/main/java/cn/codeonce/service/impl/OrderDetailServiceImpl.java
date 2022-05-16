package cn.codeonce.service.impl;

import cn.codeonce.mapper.OrderDetailMapper;
import cn.codeonce.pojo.OrderDetail;
import cn.codeonce.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * codeonce
 * OrderDetailServiceImpl
 * Yunle_TakeOut
 * 2022/5/15
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
