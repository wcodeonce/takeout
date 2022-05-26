package cn.codeonce.controller;

import cn.codeonce.common.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * codeonce
 * CommonController 文件的上传下载
 * TakeOut
 * 2022/5/10
 */
@Slf4j
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${projectpath.path}")
    private String basePath;

    // 获取项目地址
    // String path = System.getProperty("user.dir");
    // String nowPath = path + File.separator + basePath;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 操作响应
     */
    @PostMapping("/upload")
    public R<String> fileUpload(MultipartFile file) {
        // file 是一个临时文件，需要转存到指定位置，否则请求完成后文件就会删除
        log.info("上传文件信息=>{}", file.toString());

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 使用UUID重新生成文件名称，防止文件名称重复造成的文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        // 创建一个目录对象
        File dir = new File(basePath);
        // 判断当前目录是否存在
        if (!dir.exists()) {
            // 目录不存在，需要创建一个目录
            dir.mkdirs();
        }

        // 转存文件
        try {
            // 将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 返回文件上传名称
        return R.success(fileName);
    }


    /**
     * 文件下载
     *
     * @param name     文件名称
     * @param response 文件响应
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            // 通过文件输入流读取文件内容
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));

            // 通过输出流回写浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            // 声明响应数据类型
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            // 读取文件数据
            while ((len = inputStream.read(bytes)) != -1) {
                // 回写文件数据
                outputStream.write(bytes, 0, len);
                // 刷新数据
                outputStream.flush();
            }

            // 关闭文件读写器
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
