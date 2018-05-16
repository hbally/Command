package com.cabinet.command.crc.mode;

import android.os.Handler;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Description : 驱动任务抽象类
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/4
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Driver {
    /**
     * 执行任务
     */
    void runTask();

    /**
     * 开启串口
     */
    void startUsb();

    /**
     * 开启串口
     */
    void startUsb(UsbCallback callback);

    /**
     * 获取驱动
     * @return
     */
    CH34xUARTDriver driver();

    /**
     * 提供统一handler
     * @return
     */
    DriverHandler getHandler();

    /**
     * 初始化状态回调
     */
    public interface UsbCallback {
        void onSuccess();
        void onError();
    }

    UsbCallback DEFAULT_CALLBACK = new UsbCallback() {

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    };

}
