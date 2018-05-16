package com.cabinet.command.crc.mode;

import com.cabinet.command.crc.intercenter.Interceptor;

/**
 * Description : 指令包装类
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Command {
    // 发送的指令
    public byte[] command;

    // 拦截器 里面包含拦截的指令
    public Interceptor interceptor;

    public Command(byte[] command, Interceptor interceptor) {
        this.command = command;
        this.interceptor = interceptor;
        if (interceptor == null){
            this.interceptor = new Interceptor(command);
        }
    }
}
