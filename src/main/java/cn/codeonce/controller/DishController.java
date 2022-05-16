package cn.codeonce.controller;

import cn.codeonce.common.R;
import cn.codeonce.dto.DishDto;
import cn.codeonce.pojo.Category;
import cn.codeonce.pojo.Dish;
import cn.codeonce.pojo.DishFlavor;
import cn.codeonce.service.CategoryService;
import cn.codeonce.service.DishFlavorService;
import cn.codeonce.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * codeonce
 * DishController 菜品管理
 * Yunle_TakeOut
 * 2022/5/10
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 菜品信息分页查询
     *
     * @param page     页数
     * @param pageSize 每页数据
     * @param name     搜索菜品名称
     * @return 菜品信息
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        log.info("菜品页面分页信息：page=>{}, pageSize=>{}, name=>{}", page, pageSize, name);

        // 分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // 条件构造器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        // 添加排序条件
        wrapper.orderByDesc(Dish::getUpdateTime);

        // 执行分页查询
        dishService.page(pageInfo, wrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        // 遍历 records ，创建 dishDto 对象，将属性设置给 dishDto
        List<DishDto> dishDtoList = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            // 对象拷贝
            BeanUtils.copyProperties(item, dishDto);
            // 分类ID
            Long categoryId = item.getCategoryId();
            // 根据ID查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                // 将查询出来的菜品名称拷贝到 dishDto
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            // 返回 dishDto 对象
            return dishDto;
        }).collect(Collectors.toList());

        // 将 dishDto 对象的值赋给 dishDtoPage
        dishDtoPage.setRecords(dishDtoList);

        // 返回数据
        return R.success(dishDtoPage);
    }


    /**
     * 新增菜品
     *
     * @param dishDto 菜品信息封装对象
     * @return 操作反馈
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品信息=>{}" + dishDto.toString());

        // 执行保存操作
        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品信息成功!");
    }


    /**
     * 根据Id查询菜品信息和对应口味信息
     *
     * @param id 菜品id
     * @return 菜品信息
     */
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable Long id) {
        // 执行查询
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        // 返回数据
        return R.success(dishDto);
    }


    /**
     * 更新菜品信息同时更新对应的口味信息
     *
     * @param dishDto 菜品信息封装对象
     * @return 操作反馈
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("更新菜品信息=>{}", dishDto.toString());

        // 执行保存操作
        dishService.updateWithFlavor(dishDto);

        return R.success("更新菜品信息成功!");
    }


    /**
     * 根据条件查询对应的菜品数据
     *
     * @param dish 菜品条件
     * @return 菜品数据
     */
    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        // 条件构造
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());

        // 查询状态为 1 （启售）的菜品
        wrapper.eq(Dish::getStatus, 1);

        // 结果排序
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(wrapper);

        return R.success(list);
    }*/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        // 条件构造
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());

        // 查询状态为 1 （启售）的菜品
        wrapper.eq(Dish::getStatus, 1);

        // 结果排序
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(wrapper);

        // 遍历 records ，创建 dishDto 对象，将属性设置给 dishDto
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            // 对象拷贝
            BeanUtils.copyProperties(item, dishDto);
            // 分类ID
            Long categoryId = item.getCategoryId();
            // 根据ID查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                // 将查询出来的菜品名称拷贝到 dishDto
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            // 当前菜品的ID
            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishId);
            // SQL: select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);

            // 口味信息添加到 dishDto 做返回
            dishDto.setFlavors(dishFlavorList);

            // 返回 dishDto 对象
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }


    /**
     * 删除菜品信息
     *
     * @param ids 要删除菜品的id集合
     * @return 操作响应
     */
    @DeleteMapping
    @Transactional
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("要删除的菜品ID集合=>{}", ids.toString());

        // 执行删除操作，先删除菜品信息，再删除对应口味信息
        dishService.removeWithFlavor(ids);

        return R.success("删除菜品信息成功!");
    }


    /**
     * 更改菜品的售卖状态
     *
     * @param type 要更改成的状态
     * @param ids  更改状态菜品的id集合
     * @return 操作响应
     */
    @PostMapping("/status/{type}")
    public R<String> status(@PathVariable Integer type, @RequestParam List<String> ids) {
        log.info("批量更改菜品状态操作=>{}", type == 1 ? "启售" : "停售");
        log.info("批量更改菜品状态ID=>{}", ids.toString());

        // 更新条件构造
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Dish::getId, ids); // id集合
        wrapper.set(Dish::getStatus, type); // 更改的状态

        // 执行更新操作
        dishService.update(wrapper);

        return R.success("更改菜品状态成功!");
    }

}
