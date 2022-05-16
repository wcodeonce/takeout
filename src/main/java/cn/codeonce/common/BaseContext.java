package cn.codeonce.common;

/**
 * 基于ThreadLocal封装的工具类
 * 用于保存和获取当前登录用户的ID
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 获取值
     *
     * @return 设置的ID值
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 设置值
     *
     * @param id 设置的ID
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

}
