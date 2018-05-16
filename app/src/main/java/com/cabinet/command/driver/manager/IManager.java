package com.cabinet.command.driver.manager;

import com.cabinet.command.driver.worker.WorkerThread;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface IManager {

    /**
     * 获取工作线程
     */
    WorkerThread worker();

    /**
     * 获取驱动
     */
    CH34xUARTDriver driver();

    /**
     * 设置驱动
     */
    void setDriver(CH34xUARTDriver driver);

    /**
     * 设置打开状态
     */
    void setOpen(boolean isOpen);

    /**
     * 获取打开状态
     */
    boolean isOpen();
}
