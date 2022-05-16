package cn.codeonce.test;

import cn.codeonce.pojo.Employee;
import cn.codeonce.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EmployeeTest {

    @Autowired
    private EmployeeService service;

    @Test
    void getAll() {
        List<Employee> list = service.list();
        for (Employee employee : list) {
            System.out.println(employee);
        }
    }

    @Test
    void LogIn() {
        String username = "admin";
        String password = "e10adc3949ba59abbe56e057f20f883e";

        QueryWrapper<Employee> wrapper = new QueryWrapper<Employee>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);

        boolean b = service.list(wrapper) == null;
        System.out.println(b);
    }

}
