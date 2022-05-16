package cn.codeonce.controller;

import cn.codeonce.common.R;
import cn.codeonce.pojo.Employee;
import cn.codeonce.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * codeonce
 * EmployeeController 员工管理
 * Yunle_TakeOut
 * 2022/5/7
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;


    /**
     * 员工登录
     *
     * @param request  session
     * @param employee 员工实体
     * @return 登录信息
     */
    @PostMapping("/login")
    public R<Employee> LogIn(HttpServletRequest request, @RequestBody Employee employee) {
        // 1.将页面提交的密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = service.getOne(queryWrapper);

        // 3.没有查询结果则返回登录失败
        if (emp == null) return R.error("用户名或密码错误!");

        // 4.查询到结果则进行密码比对 对比错误返回登录失败
        if (!emp.getPassword().equals(password)) return R.error("用户名或密码错误!");

        // 5.检查员工状态，如果已禁用，则返回员工已禁用结果
        if (emp.getStatus() == 0) return R.error("该账号已被禁用!");

        // 6.登录成功，将员工id存入Session并返回登录成功的结果
        request.getSession().setAttribute("employee", emp.getId());
        log.info("用户" + emp.getName() + "登录成功");
        return R.success(emp);
    }


    /**
     * 退出登录
     *
     * @param request session
     * @return 登录响应是否成功
     */
    @PostMapping("/logout")
    public R<String> LogOut(HttpServletRequest request) {
        // 清除Session中保存的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功!");
    }


    /**
     * 新增员工
     *
     * @param employee 前端传递员工信息
     * @return 登录响应
     */
    @PostMapping
    public R<String> Save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息： {}", employee.toString());
        // 设置初始密码 123455，进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // 创建时间 / 更新时间
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        // 获取当前用户ID
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // 创建人 / 更新人
        // employee.setCreateUser(empId);
        // employee.setUpdateUser(empId);

        // 保存用户
        service.save(employee);

        return R.success("新增员工信息成功!");
    }


    /**
     * 分页查询
     *
     * @return 分页查询数据
     */
    @GetMapping("/page")
    public R<Page<Employee>> Page(int page, int pageSize, String name) {
        log.info("员工页面分页信息：page=>{}, pageSize=>{}, name=>{}", page, pageSize, name);

        // 构造分页条件
        Page<Employee> pageInfo = new Page<Employee>(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        service.page(pageInfo, wrapper);

        // 返回响应数据
        return R.success(pageInfo);
    }


    /**
     * 更新操作
     *
     * @param employee 员工信息
     * @return 操作响应
     */
    @PutMapping
    public R<String> Update(HttpServletRequest request, @RequestBody Employee employee) {
        // log.info(String.valueOf(employee));
        // 更新者ID / 更新时间
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empId);
        // employee.setUpdateTime(LocalDateTime.now());

        long id = Thread.currentThread().getId();
        log.info("线程ID为：=>{}", id);

        // 更新员工状态
        service.updateById(employee);

        return R.success("员工信息修改成功!");
    }


    /**
     * 根据ID查询员工信息
     *
     * @param id 员工id
     * @return 查询到的员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> FindById(@PathVariable Long id) {
        log.info("查询用户的ID=> {}", id);
        // 执行查询操作
        Employee emp = service.getById(id);
        // 查询成功
        if (emp != null) return R.success(emp);
        // 查询失败
        return R.error("没有查询到对应员工信息!");
    }
}
