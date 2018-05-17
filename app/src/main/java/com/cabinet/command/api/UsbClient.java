package com.cabinet.command.api;

import org.driver.UsbRetorfit;
import org.driver.adapter.RxJava2CallAdapter;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class UsbClient {

    private static final UsbApi sUsbApi;

    static {
        UsbRetorfit usbRetorfit = new UsbRetorfit.Builder()
                .addCallAdapter(RxJava2CallAdapter.create())
                .driver(new UsbDriveImpl())
                .build();

        sUsbApi = usbRetorfit.create(UsbApi.class);
    }


    public static UsbApi getUsb() {
        return sUsbApi;
    }
}
