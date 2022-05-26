package cn.codeonce.service;

import cn.codeonce.dto.DishDto;
import cn.codeonce.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * codeonce
 * DIshService
 * TakeOut
 * 2022/5/9
 */
public interface DishService extends IService<Dish> {

    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表
    void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品信息和对应的口味信息
    DishDto getByIdWithFlavor(Long id);

    // 更新菜品信息同时更新对应的口味信息
    void updateWithFlavor(DishDto dishDto);

    // 删除菜品信息同时删除菜品和口味的关联数据
    void removeWithFlavor(List<Long> ids);
}
