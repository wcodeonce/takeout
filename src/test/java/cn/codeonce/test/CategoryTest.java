package cn.codeonce.test;

import cn.codeonce.pojo.Category;
import cn.codeonce.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * codeonce
 * CategoryTest
 * Yunle_TakeOut
 * 2022/5/10
 */
@SpringBootTest
public class CategoryTest {

    @Autowired
    private CategoryService service;

    @Test
    void getAll() {
        List<Category> categories = service.list();
        for (Category category : categories) {
            System.out.println(category);
        }
    }


}
