package cn.codeonce.service.impl;

import cn.codeonce.mapper.UserMapper;
import cn.codeonce.pojo.User;
import cn.codeonce.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * codeonce
 * UserServiceImpl
 * TakeOut
 * 2022/5/12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
