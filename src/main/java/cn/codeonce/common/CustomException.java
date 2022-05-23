package cn.codeonce.common;

/**
 * codeonce
 * CustomException 自定义业务异常
 * TakeOut
 * 2022/5/10
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

}
