package com.cabinet.command.driver.command;

import com.cabinet.command.crc.intercenter.Interceptor;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Description : 指令任务
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Command {

    /**
     * 发送指令
     */
    public byte[] command;

    /**
     * 拦截指令
     */
    public byte[] commandInterceptor;

    /**
     * 处理订阅回调
     */
    public Observer<Result> observer;

    public Command(byte[] command, byte[] commandInterceptor, Observer<Result> observer) {
        this.command = command;
        this.observer = observer;
        this.commandInterceptor = commandInterceptor;
    }

}
