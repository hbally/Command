package com.cabinet.command.api;

import android.app.Application;
import android.util.Log;

import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.rx.Rx;

import org.driver.Info;
import org.driver.Rxjava2RetryWithDelay;
import org.driver.modle.Call;
import org.driver.modle.UsbDrive;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class UsbDriveImpl implements UsbDrive {
    private static String TAG = "UsbDriveImpl";

    private static Application sApp;

    /**
     * 初始化
     */
    public static void init(Application application) {
        sApp = application;
    }

    public UsbDriveImpl() {
        if (sApp == null) {
            throw new NullPointerException("application is null ,please call method init() by application !!!!");
        }
    }

    /**
     * 发送指令
     *
     * @param info     封装了发送指令，拦截指令，重试次数，间隔时间等 ，一定需要用callback回调,否则监听器收不到结果
     * @param callback 返回结果
     */
    @Override
    public void execute(final Info info, final Call.Callback callback) {
        Rx.driver(sApp)
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            Log.e(TAG, "串口打开失败");
                        }
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<Result>>() {
                    @Override
                    public ObservableSource<Result> apply(Boolean aBoolean) throws Exception {
                        return Rx.send(info.getSend(), info.getIntercept());
                    }
                })
                // 重试
                .retryWhen(new Rxjava2RetryWithDelay(info.getRetryCount(), info.getInterval()))
                .filter(new Predicate<Result>() {
                    @Override
                    public boolean test(Result result) throws Exception {
                        // 如果失败回调失败接口
                        if (!result.isSuccess()) {
                            callback.onError(result.getMsg());
                        }
                        return result.isSuccess();
                    }
                })
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        callback.onSuccess(result.getResult());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 异常回调接口
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
