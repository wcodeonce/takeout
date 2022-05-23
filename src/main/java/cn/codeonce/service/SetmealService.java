package cn.codeonce.service;

import cn.codeonce.dto.SetmealDto;
import cn.codeonce.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * codeonce
 * SetmealService
 * TakeOut
 * 2022/5/11
 */
public interface SetmealService extends IService<Setmeal> {

    // 将套餐基本信息和关联的菜品信息保存
    public void saveWithDish(SetmealDto setmealDto);

    // 删除套餐同时删除套餐和菜品的关联数据
    public void removeWithDish(List<Long> ids);

}
