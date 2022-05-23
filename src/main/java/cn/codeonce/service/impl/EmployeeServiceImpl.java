package cn.codeonce.service.impl;

import cn.codeonce.mapper.EmployeeMapper;
import cn.codeonce.pojo.Employee;
import cn.codeonce.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * codeonce
 * EmployeeServiceImpl
 * TakeOut
 * 2022/5/7
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
