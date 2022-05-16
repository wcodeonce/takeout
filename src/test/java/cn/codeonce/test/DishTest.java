package cn.codeonce.test;

import cn.codeonce.pojo.Dish;
import cn.codeonce.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * codeonce
 * DishTest
 * Yunle_TakeOut
 * 2022/5/11
 */
@SpringBootTest
public class DishTest {

    @Autowired
    private DishService service;

    @Test
    void status() {
        Integer statusType = 1;
        String ids = "1524274966913675266,1524274580354035713,1524274496547647490";

        String[] split = ids.split(",");
        for (String s : split) {
            Dish dish = new Dish();
            dish.setId(Long.valueOf(s));
            // dish.setStatus(statusType);
            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
            // wrapper.eq(Dish::getId, s);
            wrapper.eq(Dish::getStatus, statusType);

            service.update(dish, wrapper);
        }


    }

}
