package com.cabinet.command.driver.rx.command;


import android.app.Application;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.cabinet.command.crc.intercenter.ComConfig;
import com.cabinet.command.driver.manager.Manager;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


/**
 * Description : 驱动初始化操作
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public final class ObservableDriver extends Observable<Boolean> {

    private static final String TAG = "ObservableDriver";

    private String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";
    private int BAUD_RATE = ComConfig.Backdates.BACKDATE_115200.getValue();
    private byte DATA_BIT = (byte) ComConfig.DataBits.DataBits_8.getValue();
    private byte STOP_BIT = (byte) ComConfig.StopBits.SHOPLIFTS_1.getValue();
    private byte PARITY = (byte) ComConfig.Parity.PARITY_NONE_NONE.getValue();
    private byte FLOW_CONTROL = (byte) ComConfig.FlowControl.FLOW_CONTROL_NONE.getValue();

    private Application app;

    public ObservableDriver(Application app) {
        this.app = app;
    }

    /**
     * Operator implementations (both source and intermediate) should implement this method that
     * performs the necessary business logic.
     * <p>There is no need to call any of the plugin hooks on the current Observable instance or
     * the Subscriber.
     *
     * @param observer the incoming Observer, never null
     */
    @Override
    protected void subscribeActual(Observer<? super Boolean> observer) {
        // 执行初始化操作
        Observable.just(app)
                // 主线程初始化
                .observeOn(AndroidSchedulers.mainThread())
                // 获取usb驱动服务
                .map(new Function<Application, UsbManager>() {
                    @Override
                    public UsbManager apply(Application app) throws Exception {
                        return (UsbManager) app.getSystemService(Context.USB_SERVICE);
                    }
                })
                // 初始化串口驱动
                .map(new Function<UsbManager, CH34xUARTDriver>() {
                    @Override
                    public CH34xUARTDriver apply(UsbManager usbDevice) throws Exception {
                        return new CH34xUARTDriver(usbDevice, app, ACTION_USB_PERMISSION);
                    }
                })
                // 打开串口 链接串口
                .map(new Function<CH34xUARTDriver, Boolean>() {
                    @Override
                    public Boolean apply(CH34xUARTDriver driver) throws Exception {

                        // 管理类的driver赋值
                        Manager.getInstance().setDriver(driver);

                        Log.e(TAG, "实例化CH34xUARTDriver成功");
                        boolean connected = driver.isConnected();

                        if (connected) {
                            // 连接上不需要在打开
                            return true;
                        }

                        Log.e(TAG, "CH34xUARTDriver不是连接的");

                        if (!driver.UsbFeatureSupported()) {
                            Log.e(TAG, "系统不支持USB HOST");
                            return false;
                        }

                        Log.e(TAG, "系统支持USB HOST");

                        // 链接driver
                        int rectal = driver.ResumeUsbList();

                        if (rectal == -1) {
                            Log.e(TAG, "打开设备失败!\n");
                            return false;
                        }
                        if (rectal != 0) {
                            Log.e(TAG, "没有发现设备，请检查你的设备是否正常\n");
                            return false;
                        }

                        //对串口设备进行初始化操作
                        boolean uartInit = driver.UartInit();

                        if (!uartInit) {
                            Log.e(TAG, "设备初始化失败!\n");
                            return false;
                        }
                        Log.e(TAG, "打开设备成功!\n");
                        //配置串口波特率
                        boolean config = driver.SetConfig(BAUD_RATE, DATA_BIT, STOP_BIT, PARITY, FLOW_CONTROL);

                        if (config) {
                            Log.e(TAG, "串口设置成功!\n");
                            return true;
                        }

                        Log.e(TAG, "串口设置失败!\n");
                        return false;
                    }
                })
                // 成功启动工作线程
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Manager.getInstance().worker().work();
                            Manager.getInstance().setOpen(true);
                        }
                        return aBoolean;
                    }
                })
                .subscribe(new DriverObserver<>(observer));
    }


    /**
     * drive订阅
     */
    final class DriverObserver<T> implements Observer<T> {

        private Observer<? super T> observer;

        public DriverObserver(Observer<? super T> observer) {
            this.observer = observer;
        }


        @Override
        public void onSubscribe(Disposable d) {
            observer.onSubscribe(d);
        }


        @Override
        public void onNext(T o) {
            observer.onNext(o);
        }


        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }


        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }

}
