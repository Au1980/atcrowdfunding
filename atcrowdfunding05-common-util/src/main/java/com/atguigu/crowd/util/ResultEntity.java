package com.atguigu.crowd.util;

/**
 * 统一整个项目中Ajax请求返回的结果(未来也可以用于分布式架构各个模块间调用时返回的类型)
 * @param <T>
 */
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    // 用来封装当前请求处理的结果是成功还是失败
    private String result;

    // 请求处理失败返回的错误消息
    private String message;

    // 要返回的数据
    private T data;

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 请求处理成功且不需要返回数据时使用的工具方法
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> successWithoutData() {
        return new ResultEntity<T>(SUCCESS, null, null);
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> successWithData(T data) {
        return new ResultEntity<T>(SUCCESS, null, data);
    }

    /**
     * 请求处理失败后使用的工具方法
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> failed(String message) {
        return new ResultEntity<T>(FAILED, message, null);
    }
}
