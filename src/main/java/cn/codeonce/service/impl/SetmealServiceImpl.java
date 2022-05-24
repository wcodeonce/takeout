package cn.codeonce.service.impl;

import cn.codeonce.common.CustomException;
import cn.codeonce.dto.SetmealDto;
import cn.codeonce.mapper.SetmealMapper;
import cn.codeonce.pojo.Setmeal;
import cn.codeonce.pojo.SetmealDish;
import cn.codeonce.service.SetmealDishService;
import cn.codeonce.service.SetmealService;
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
 * SetmealServiceImpl
 * TakeOut
 * 2022/5/10
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 将套餐基本信息和关联的菜品信息保存
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息 操作 setmeal 执行 insert 操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的关联信息 setmealDish 执行 insert 操作
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 根据id查询套餐信息和对应的菜品信息
     *
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        // 查询套餐基本信息
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();

        // 对象拷贝
        BeanUtils.copyProperties(setmeal, setmealDto);

        // 查询套餐对应的菜品信息
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmeal.getId());

        // 执行查询操作
        List<SetmealDish> list = setmealDishService.list(wrapper);

        // dishDto 赋值菜品口味信息
        setmealDto.setSetmealDishes(list);

        return setmealDto;
    }

    /**
     * 更新套餐基本信息的同时更新菜品信息
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        // 更新dish表基本信息
        this.updateById(setmealDto);

        // 清理当前套餐对应的菜品信息
        // 构造条件
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        // 执行删除操作
        setmealDishService.remove(wrapper);

        // 添加当前提交过来的口味信息
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();

        dishes = dishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(dishes);
    }


    /**
     * 删除套餐同时删除套餐和菜品的关联数据
     *
     * @param ids 要删除套餐的id集合
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，判断是否可以删除
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getStatus, 1);
        wrapper.in(Setmeal::getId, ids);

        // 执行查询操作
        long count = this.count(wrapper);

        // 如果不能删除，直接抛出业务异常
        if (count > 0) throw new CustomException("套餐正在售卖中，不能删除!");

        // 如果可以删除，先删除套餐表中的数据 setmeal
        this.removeByIds(ids);

        // 再删除关系表中的数据 setmeal_dish
        // 条件构造
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getId, ids);
        // 执行删除操作
        setmealDishService.remove(queryWrapper);
    }

}
