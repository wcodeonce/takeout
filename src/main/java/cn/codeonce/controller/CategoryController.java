package cn.codeonce.controller;

import cn.codeonce.common.R;
import cn.codeonce.pojo.Category;
import cn.codeonce.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * codeonce
 * CategoryController 分类管理
 * TakeOut
 * 2022/5/10
 */
@Slf4j
@Api(tags = "分类相关接口")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    /**
     * 新增分类
     *
     * @param category 分类
     * @return 响应信息
     */
    @PostMapping
    public R<String> Save(@RequestBody Category category) {
        log.info("新增分类=> {}", category);
        service.save(category);
        return R.success("新增分类成功!");
    }


    /**
     * 分页查询
     *
     * @param page     每页数据
     * @param pageSize 每页数据条数
     * @return 分页数据
     */
    @GetMapping("/page")
    public R<Page<Category>> Page(int page, int pageSize) {
        log.info("分类页面分页信息：page=>{}, pageSize=>{}", page, pageSize);

        // 构造分页条件
        Page<Category> pageInfo = new Page<Category>(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        // 添加排序条件
        wrapper.orderByDesc(Category::getSort);

        // 分页查询
        service.page(pageInfo, wrapper);

        return R.success(pageInfo);
    }


    /**
     * 根据ID删除分类
     *
     * @param ids 分类id
     * @return 操作响应
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类ID为=> {}", ids);

        // 执行根据id删除
        service.remove(ids);

        return R.success("分类信息删除成功!");
    }


    /**
     * 更新菜品信息
     *
     * @param category 菜品
     * @return 操作响应
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        service.updateById(category);
        return R.success("菜品信息更新成功!");
    }


    /**
     * 条件获取菜品分类信息
     *
     * @param category 菜品
     * @return 分类列表
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        log.info("菜品分类信息=>{}", category.getType());

        // 条件构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        // 添加条件
        wrapper.eq(category.getType() != null, Category::getType, category.getType());
        // 添加排序条件
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        // 执行查询操作
        List<Category> categoryList = service.list(wrapper);

        // 返回查询到的分类信息
        return R.success(categoryList);
    }
}
