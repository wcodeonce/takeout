package cn.codeonce.service.impl;

import cn.codeonce.mapper.ShoppingCartMapper;
import cn.codeonce.pojo.ShoppingCart;
import cn.codeonce.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * codeonce
 * ShoppingCartServiceImpl
 * TakeOut
 * 2022/5/14
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
