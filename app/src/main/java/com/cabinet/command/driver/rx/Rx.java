package com.cabinet.command.driver.rx;


import android.app.Application;

import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.rx.command.ObservableDriver;
import com.cabinet.command.driver.rx.command.ObservableInterceptor;
import com.cabinet.command.driver.rx.command.ObservableSend;

import io.reactivex.Observable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Description : 指令操作相关类
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public abstract class Rx extends Observable<byte[]> {

    /**
     * 只发送,不拦截
     */
    public static  Observable<Result> send(byte[] command){
        // 发送指令和拦截指令一样
        return send(command,command);
    }

    /**
     * 发送指令并拦截所给指令
     *
     * @param command  发送指令
     * @param interceptor  拦截指令
     */
    public static Observable<Result> send(byte[] command, byte[] interceptor){

        ObjectHelper.requireNonNull(command, "command is null");
        ObjectHelper.requireNonNull(interceptor, "interceptor is null");

        return RxJavaPlugins.onAssembly(new ObservableSend(command,interceptor));
    }


    /**
     * 不发送指令 只拦截指令
     */
    public static Observable<Result> interceptor(byte[] interceptor){

        ObjectHelper.requireNonNull(interceptor, "interceptor is null");

        return RxJavaPlugins.onAssembly(new ObservableInterceptor(interceptor));
    }


    /**
     * 初始化设备
     *
     * @return  Boolean  是否成功
     */
    public static Observable<Boolean> driver(Application app){

        ObjectHelper.requireNonNull(app, "application is null");

        return RxJavaPlugins.onAssembly(new ObservableDriver(app));
    }
}
