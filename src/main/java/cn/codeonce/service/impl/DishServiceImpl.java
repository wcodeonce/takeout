package cn.codeonce.service.impl;

import cn.codeonce.common.CustomException;
import cn.codeonce.dto.DishDto;
import cn.codeonce.mapper.DishMapper;
import cn.codeonce.pojo.Dish;
import cn.codeonce.pojo.DishFlavor;
import cn.codeonce.service.DishFlavorService;
import cn.codeonce.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * codeonce
 * DIshServiceImpl
 * TakeOut
 * 2022/5/9
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品 同时保存口味数据
     *
     * @param dishDto 菜品口味数据
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品基本信息到菜品表 dish
        this.save(dishDto);

        // dishId 菜品的ID
        Long dishId = dishDto.getId();

        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存菜品口味信息到菜品口味表 dishFlavor
        dishFlavorService.saveBatch(flavors);

    }


    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id 菜品id
     * @return 菜品信息
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        // 对象拷贝
        BeanUtils.copyProperties(dish, dishDto);

        // 查询菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());

        // 执行查询操作
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);

        // dishDto 赋值菜品口味信息
        dishDto.setFlavors(flavors);

        return dishDto;
    }


    /**
     * 更新菜品信息同时更新对应的口味信息
     *
     * @param dishDto dishDto封装
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表基本信息
        this.updateById(dishDto);

        // 清理当前菜品对应的口味信息
        // 构造条件
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        // 执行删除操作
        dishFlavorService.remove(wrapper);

        // 添加当前提交过来的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }


    /**
     * 删除菜品信息同时删除菜品和口味的关联数据
     *
     * @param ids 要删除的菜品id集合
     */
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {
        // 查询套餐状态，判断是否可以删除
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, 1);
        wrapper.in(Dish::getId, ids);

        // 执行查询操作
        long count = this.count(wrapper);

        // 如果不能删除，直接抛出业务异常
        if (count > 0) throw new CustomException("菜品正在售卖中，不能删除!");

        // 如果可以删除，先删除套餐表中的数据 dish
        this.removeByIds(ids);

        // 再删除关系表中的数据 dish_flavor
        // 条件构造
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getId, ids);

        // 执行删除操作
        dishFlavorService.remove(queryWrapper);
    }
}
