package com.cabinet.command.driver.worker.abs;

/**
 * Description : 任务执行接口
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface IHandler {

    /**
     * 执行任务
     */
    void run();

    /**
     * 延时执行
     */
    void runDelayed(long time);


    /**
     * 是否延时
     */
    boolean isDelayed();
}
