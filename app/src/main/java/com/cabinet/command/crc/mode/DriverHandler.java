package com.cabinet.command.crc.mode;

import android.os.Handler;
import android.os.Message;

import com.cabinet.command.crc.intercenter.Interceptor;

import static com.cabinet.command.crc.mode.WhatConfig.*;

/**
 * Description : 线程任务的handler
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class DriverHandler extends Handler{


    /**
     * @param callback Task 实现了callback
     */
    public DriverHandler(Handler.Callback callback){
        super(callback);
    }


    /**
     * 拦截器 发送指令
     */
    public void sendCommand(Command command){
        Message message = obtainMessage();
        message.what = ADD_INTERCEPTOR;
        message.obj = command;
        sendMessage(message);
    }

}
