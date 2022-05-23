package cn.codeonce.service;

import cn.codeonce.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * codeonce
 * CategoryService
 * TakeOut
 * 2022/5/10
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
