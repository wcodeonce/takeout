package cn.codeonce.controller;

import cn.codeonce.common.BaseContext;
import cn.codeonce.common.R;
import cn.codeonce.pojo.ShoppingCart;
import cn.codeonce.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * codeonce
 * ShoppingCartController
 * TakeOut
 * 2022/5/14
 */
@Slf4j
@Api(tags = "购物车相关接口")
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService service;


    /**
     * 查看购物车
     *
     * @return 购物车数据
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车信息");

        // 获取当前用户id
        Long currentId = BaseContext.getCurrentId();

        // 条件构造
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);
        // 条件排序
        wrapper.orderByAsc(ShoppingCart::getCreateTime);

        // 执行查询
        List<ShoppingCart> list = service.list(wrapper);

        // 返回数据
        return R.success(list);
    }


    /**
     * 添加菜品套餐到购物车
     *
     * @param shoppingCart 菜品套餐信息
     * @return 添加成功后的菜品套餐
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车=>{}", shoppingCart);

        // 设置用户id，指定当前用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        // 插入时间
        shoppingCart.setCreateTime(LocalDateTime.now());
        // 当前用户id
        shoppingCart.setUserId(currentId);

        // 查询当前菜品套餐是否已在购物车
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);

        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            // 添加的是菜品
            wrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            // 添加的是套餐
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // 执行查询稍作 查询套餐或套餐是否在购物车中
        // SQL: select * from shopping_cat where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartInfo = service.getOne(wrapper);

        if (cartInfo != null) {
            // 如果购物车中已经存在该菜品套餐，在当前购物车菜品套餐的数量+1即可
            Integer number = cartInfo.getNumber();
            cartInfo.setNumber(number += 1);
            // 更新
            service.updateById(cartInfo);
        } else {
            // 如果不存在着添加到购物车，数量默认为 1
            shoppingCart.setNumber(1);
            service.save(shoppingCart);
            // 保存购物车
            cartInfo = shoppingCart;
        }

        // 返回购物车对象
        return R.success(cartInfo);
    }


    /**
     * 更改购物车中菜品套餐
     *
     * @param shoppingCart 购物车中菜品套餐信息
     * @return 操作响应
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info("减少购物车菜品套餐=>{}", shoppingCart);

        // 设置用户id，指定当前用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        // 当前用户id
        shoppingCart.setUserId(currentId);

        // 查询当前菜品套餐是否已在购物车
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);

        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            // 减少数量的是菜品
            wrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            // 减少数量的是套餐
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // 执行查询稍作 查询套餐或套餐是否在购物车中
        // SQL: select * from shopping_cat where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartInfo = service.getOne(wrapper);

        // 判断该菜品套餐还剩几份
        if (cartInfo.getNumber() == 1) {
            // 如果购物车中菜品或套餐只剩一份继续减少 则删除该菜品套餐
            service.removeById(cartInfo);
        } else {
            // 如果不止一份则当前数量 -1
            Integer number = cartInfo.getNumber();
            cartInfo.setNumber(number -= 1);
            service.updateById(cartInfo);
        }

        return R.success("更改商品状态成功!");
    }


    /**
     * 清空购物车
     *
     * @return 操作响应
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        // 条件构造
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        // 执行清空操作
        service.remove(wrapper);

        // 操作响应
        return R.success("清空购物车成功!");
    }

}
