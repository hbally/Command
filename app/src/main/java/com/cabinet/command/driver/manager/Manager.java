package com.cabinet.command.driver.manager;

import com.cabinet.command.driver.worker.WorkerThread;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Description : 管理类
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Manager implements IManager {

    private static volatile Manager sManager;

    static {
        sManager = new Manager();
    }

    /**
     * 串口驱动
     */
    private CH34xUARTDriver driver;

    /**
     * 工作线程
     */
    private WorkerThread work;

    /**
     * 是否打开串口
     */
    private boolean isOpenDriver;

    private Manager() {
        work = new WorkerThread();
    }

    public static Manager getInstance() {
        return sManager;
    }

    /**
     * 获取工作线程
     */
    @Override
    public WorkerThread worker() {
        return work;
    }

    /**
     * 获取驱动
     */
    @Override
    public CH34xUARTDriver driver() {
        return driver;
    }

    /**
     * 设置驱动
     *
     * @param driver
     */
    @Override
    public void setDriver(CH34xUARTDriver driver) {
        this.driver = driver;
    }

    /**
     * 设置打开状态
     *
     * @param isOpen
     */
    @Override
    public void setOpen(boolean isOpen) {
        isOpenDriver = isOpen;
    }

    /**
     * 获取打开状态
     */
    @Override
    public boolean isOpen() {
        return isOpenDriver;
    }
}
