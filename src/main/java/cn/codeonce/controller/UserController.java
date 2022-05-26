package cn.codeonce.controller;

import cn.codeonce.common.R;
import cn.codeonce.pojo.User;
import cn.codeonce.service.UserService;
import cn.codeonce.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * codeonce
 * UserController
 * TakeOut
 * 2022/5/12
 */
@Slf4j
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private StringRedisTemplate redis;

    /**
     * 发送手机短信验证码
     *
     * @param user 前端传入的手机号封装成用户
     * @return 操作响应
     */
    @PostMapping("/sendMsg")
    public R<String> SendMsg(@RequestBody User user, HttpSession session) {
        log.info("登录手机号=>{}", user.getPhone());

        // 获取手机号
        String phone = user.getPhone();

        if (StringUtils.isEmpty(phone)) return R.error("短信验证失败!");

        // 生成随机4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();

        log.info("短信注册的随机验证码=>{}", code);

        // 调用阿里云短信服务API发送短信
        // SMSUtils.sendMessage("温柔且无情个人网", "1400537", phone, code);

        // 将生成的验证码保存到Session
        // session.setAttribute(phone, code);

        // 将生成的验证码缓存到 Redis 中，并且设置有效期为 5 分钟
        redis.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        return R.success("手机验证码短信发送成功!");
    }


    /**
     * 移动端用户登录
     *
     * @param map     用户登录信息
     * @param session session存储用户信息
     * @return 操作响应
     */
    @PostMapping("/login")
    public R<User> Login(@RequestBody Map<String, String> map, HttpSession session) {
        log.info("登录用户信息=>{}", map.toString());

        // 获取手机号
        String phone = map.get("phone").toString();

        // 获取验证码
        String code = map.get("code").toString();

        log.info("验证码=>{}", code);

        // 从 Session 中获取保存的验证码
        // Object codeInSession = session.getAttribute(phone);

        // 从 Redis 中获取缓存的验证码
        String codeInSession = redis.opsForValue().get(phone);

        log.info("Session验证码=>{}", codeInSession);

        // 进行验证码的比对（页面提交的验证码和Session中保存的验证码对比）
        if (codeInSession == null || !codeInSession.equals(code)) return R.error("用户登录失败!");

        // 比对成功——登录成功

        // 判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);

        // 查询用户是否注册
        User user = service.getOne(wrapper);
        if (user == null) {
            // 自动完成注册
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            // 插入数据
            service.save(user);
        }
        // 将用户id放到session中
        session.setAttribute("user", user.getId());

        // 如果用户登录成功，删除 Redis 中缓存的验证码
        redis.delete(phone);

        // 用户登录成功
        return R.success(user);

    }

}
