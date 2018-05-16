package com.cabinet.command.driver.command;

/**
 * Description : 返回结果
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Result {

    /**
     * 是否成功
     */
    private boolean isSuccess;

    /**
     * 结果
     */
    private byte[] result;

    /**
     * 元婴
     */
    private String msg;

    public Result() {
        this(false);
    }

    public Result(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Result(byte[] result) {
        this(true, result);
    }

    public Result(boolean isSuccess, byte[] result) {
        this.isSuccess = isSuccess;
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }
}
