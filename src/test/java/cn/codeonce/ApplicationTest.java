package cn.codeonce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.File;
import java.util.Set;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private StringRedisTemplate redis;

    @Test
    void testRedis() {
        Set<String> keys = redis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }
        redis.opsForValue().set("username", "password");
    }

    @Test
    void contextLoads() {
        String path = System.getProperty("user.dir");

        System.out.println(path);

        String now = path + File.separator + "dir";

        System.out.println(now);
    }

}
