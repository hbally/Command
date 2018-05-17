package com.cabinet.command;

import android.app.Application;

import com.cabinet.command.api.UsbDriveImpl;
import com.cabinet.command.driver.rx.Rx;


/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Rx.driver(this)
                .subscribe();
        UsbDriveImpl.init(this);
    }
}
