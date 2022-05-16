package cn.codeonce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class ApplicationTest {

    @Test
    void contextLoads() {
        String path = System.getProperty("user.dir");

        System.out.println(path);

        String now = path + File.separator + "dir";

        System.out.println(now);
    }

}
