package com.cabinet.command.crc;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cabinet.command.crc.intercenter.Interceptor;
import com.cabinet.command.crc.mode.DriveQuest;
import com.cabinet.command.crc.mode.Driver;
import com.cabinet.command.crc.mode.DriverHandler;
import com.cabinet.command.crc.mode.Task;
import com.cabinet.command.crc.mode.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Description : 串口管理初始化系列操作
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/4
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class DriverManager extends Thread implements Driver {

    public static final String TAG = "Driver";
    private static volatile DriverManager sManager = new DriverManager();
    /**
     * 控制线程任务的交互的handler
     */
    private DriverHandler mDriverHander;
    private Task mTask;
    private CH34xUARTDriver mDriver;
    // 是否支持usb
    private boolean isSupportUsb;
    // 参数
    private DriveQuest mQuest;
    //拦击器集合
    private List<Interceptor> mInterceptors;

    private DriverManager() {
        super("Driver");
        mTask = new Task();
    }

    public static DriverManager getInstance() {
        return sManager;
    }

    @Override
    public void run() {
        Looper.prepare();
        mInterceptors =  new ArrayList<>();
        // Task 实现了接口 Handler.Callback
        mDriverHander = new DriverHandler(mTask);
        Looper.loop();
    }


    /**
     * 初始化
     */
    public void init(DriveQuest quest) {
        mQuest = quest;
        // 必须在主线程初始化
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new IllegalStateException("init must be main thread.");
        UsbManager usbService = (UsbManager) quest.app().getSystemService(Context.USB_SERVICE);
        mDriver = new CH34xUARTDriver(usbService, quest.app(), quest.usbPermission());

        Log.e(TAG, "实例化CH34xUARTDriver成功");

        if (mDriver.isConnected()) return;

        Log.e(TAG, "CH34xUARTDriver不是连接的");

        if (!mDriver.UsbFeatureSupported()) {
            Log.e(TAG, "系统不支持USB HOST");
            return;
        }

        Log.e(TAG, "系统支持USB HOST");
        isSupportUsb = true;

    }

    /**
     * 执行任务
     */
    @Override
    public void runTask() {
        // Task 实现了接口 Runnable
        mDriverHander.postDelayed(mTask,50);
    }

    /**
     * 开启串口
     */
    @Override
    public void startUsb() {
        startUsb(DEFAULT_CALLBACK);
    }

    /**
     * 开启串口
     */
    @Override
    public void startUsb(UsbCallback callback) {

        if (Looper.myLooper() != Looper.getMainLooper()){
            throw new IllegalStateException("startUsb must be main thread.");
        }

        if (mQuest == null){
            throw new IllegalStateException("startUsb must call init method.");
        }


        if (!isSupportUsb) {
            // 不支持usb驱动
            Log.e(TAG, "系统不支持USB HOST");
            callback.onError();
        }
        boolean openDriver = Utils.openDriver(mQuest);
        if (openDriver) {
            // 开启线程执行run函数
            start();
            //打开串口成功
            callback.onSuccess();
        } else {
            //打开串口失败
            callback.onError();
        }
    }

    /**
     * 获取驱动
     *
     * @return
     */
    @Override
    public CH34xUARTDriver driver() {
        return mDriver;
    }

    @Override
    public DriverHandler getHandler() {
        return mDriverHander;
    }

    /**
     * 获取拦截器集合
     *
     * @return
     */
    public List<Interceptor> getInterceptors(){
        return mInterceptors;
    }

}
