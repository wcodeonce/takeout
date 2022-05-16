package cn.codeonce.service.impl;

import cn.codeonce.common.CustomException;
import cn.codeonce.mapper.CategoryMapper;
import cn.codeonce.pojo.Category;
import cn.codeonce.pojo.Dish;
import cn.codeonce.pojo.Setmeal;
import cn.codeonce.service.CategoryService;
import cn.codeonce.service.DishService;
import cn.codeonce.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * codeonce
 * CategoryServiceImpl
 * Yunle_TakeOut
 * 2022/5/9
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    // 菜品 Service
    @Autowired
    private DishService dishService;

    // 套餐 Service
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据ID删除分类，删除之前需要进行判断
     *
     * @param id 分类id
     */
    @Override
    public void remove(Long id) {

        // 查询当前分类是否关联了菜品，如果已经关联直接抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        // 执行查询
        long dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0) {
            // 已关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除!");
        }


        // 查询当前分类是否关联了套餐，如果已经关联直接抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        // 执行查询
        long setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount > 0) {
            // 已关联套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除!");
        }

        // 判断通过，调用父类 removeById 删除分类
        super.removeById(id);
    }
}
