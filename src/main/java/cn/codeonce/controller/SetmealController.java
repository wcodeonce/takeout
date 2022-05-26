package cn.codeonce.controller;

import cn.codeonce.common.R;
import cn.codeonce.dto.SetmealDto;
import cn.codeonce.pojo.Category;
import cn.codeonce.pojo.Setmeal;
import cn.codeonce.service.CategoryService;
import cn.codeonce.service.SetmealDishService;
import cn.codeonce.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * codeonce
 * SetmealController
 * TakeOut
 * 2022/5/11
 */
@Slf4j
@Api(tags = "套餐相关接口")
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 套餐信息分页查询
     *
     * @param page     页数
     * @param pageSize 页面数据条数
     * @param name     套餐信息
     * @return 套餐分页信息
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        log.info("套餐页面分页信息：page=>{}, pageSize=>{}, name=>{}", page, pageSize, name);

        // 分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        // 条件构造器
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        // 添加排序条件
        wrapper.orderByDesc(Setmeal::getUpdateTime);

        // 执行查询
        setmealService.page(pageInfo, wrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        // 遍历 records ，创建 setmealDto 对象，将属性设置给 setmealDto
        List<SetmealDto> setmealDtoList = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            // 分类ID
            Long categoryId = item.getCategoryId();
            // 根据ID查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                // 将查询出来的菜品名称拷贝到 setmealDto
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            // 返回 setmealDto 对象
            return setmealDto;
        }).collect(Collectors.toList());

        // 将 dishDto 对象的值赋给 dishDtoPage
        setmealDtoPage.setRecords(setmealDtoList);

        // 返回分页数据
        return R.success(setmealDtoPage);
    }


    /**
     * 根据ID查询对应的套餐信息
     *
     * @param id 套餐id
     * @return 套餐信息
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getDish(@PathVariable Long id) {
        // 执行查询
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);

        // 返回数据
        return R.success(setmealDto);
    }


    /**
     * 新增套餐信息
     *
     * @param setmealDto 套餐信息
     * @return 操作响应
     */
    @CacheEvict(value = "setmealCache", allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("保存套餐信息=>{}", setmealDto.toString());

        // 执行插入操作
        setmealService.saveWithDish(setmealDto);

        // 操作成功返回
        return R.success("新增套餐信息成功!");
    }


    /**
     * 删除套餐
     *
     * @param ids 要删除的套餐id数组
     * @return 操作响应
     */
    @CacheEvict(value = "setmealCache", allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("要删除的套餐ID=>{}", ids.toString());

        // 执行删除操作，先删除套餐信息，再删除对应菜品信息
        setmealService.removeWithDish(ids);

        return R.success("删除套餐成功!");
    }


    /**
     * 更改套餐的售卖状态
     *
     * @param type 要更改成的状态
     * @param ids  更改状态套餐的id集合
     * @return 操作响应
     */
    @PostMapping("/status/{type}")
    public R<String> status(@PathVariable Integer type, @RequestParam List<String> ids) {
        log.info("批量更改套餐状态操作=>{}", type == 1 ? "启售" : "停售");
        log.info("批量更改套餐状态ID=>{}", ids.toString());

        // 更新条件构造
        LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Setmeal::getId, ids); // id集合
        wrapper.set(Setmeal::getStatus, type); // 更改的状态

        // 执行更新操作
        setmealService.update(wrapper);

        return R.success("更改套餐状态成功!");
    }


    /**
     * 更新套餐数据
     *
     * @param setmealDto 要更新套餐数据
     * @return 操作响应
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("更新菜品信息=>{}", setmealDto.toString());

        // 执行保存操作
        setmealService.updateWithDish(setmealDto);

        // 清理所有菜品的缓存数据
        // Set keys = redisTemplate.keys("dish_*");
        // log.info("清理所有的菜品缓存数据=>{}", keys);
        // redisTemplate.delete(keys);

        // 只清理某个分类下面的菜品缓存数据
        // String key = "dish_" + setmealDto.getCategoryId() + "_1";
        // redisTemplate.delete(key);

        return R.success("更新套餐信息成功!");
    }


    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal 条件
     * @return 数据
     */
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId+'_'+#setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        // 条件构造
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        // 数据排序
        wrapper.orderByDesc(Setmeal::getUpdateTime);

        // 查询数据
        List<Setmeal> list = setmealService.list(wrapper);

        return R.success(list);
    }


}
