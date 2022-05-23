package cn.codeonce.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * codeonce
 * GlobalExceptionHandler
 * TakeOut
 * 2022/5/9
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 完整性约束违反异常处理
     *
     * @return 响应
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> ExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        // 判断异常信息
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String message = split[2] + "已存在!";
            return R.error(message);
        }
        return R.error("未知错误!");
    }


    /**
     * 异常处理方法-自定义异常
     *
     * @return 响应
     */
    @ExceptionHandler(CustomException.class)
    public R<String> ExceptionHandler(CustomException ex) {
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
