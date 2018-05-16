package com.cabinet.command.crc;

import com.cabinet.command.crc.intercenter.Interceptor;
import com.cabinet.command.crc.mode.Command;
import com.cabinet.command.crc.observer.ObservableSend;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RxCommond {

    /**
     * 发送的指令
     */
    private byte[] mSendCommand;
    /**
     * 需要拦截的指令
     */
    private byte[] mInterceptorCommand;

    private RxCommond(byte[] command) {
        this.mSendCommand = command;
    }


    public static RxCommond create(byte[] sendCommand) {
        return new RxCommond(sendCommand);
    }

    public RxCommond interceptor(byte[] interceptorCommand) {
        this.mInterceptorCommand = interceptorCommand;
        return this;
    }

    public Observable<Byte[]> observer() {

        // 发送的指令必须不为空
        if (mSendCommand == null) {
            throw new NullPointerException("send command is null!");
        }

        // 如果不需要拦截其他指令 则拦截发送指令
        if (mInterceptorCommand == null) {
            mInterceptorCommand = mSendCommand;
        }

        // 创建事件的根
        return ObservableSend.send(new Command(mSendCommand, new Interceptor(mInterceptorCommand)));
    }
}
