package com.cabinet.command;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cabinet.command.api.UsbClient;
import com.cabinet.command.crc.RxCommond;
import com.cabinet.command.crc.intercenter.Interceptor;
import com.cabinet.command.crc.mode.Driver;
import com.cabinet.command.crc.DriverManager;
import com.cabinet.command.crc.mode.DriveQuest;
import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.rx.Rx;
import com.cabinet.command.utils.CommandAssemble;
import com.cabinet.command.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        UsbClient.getUsb().check().subscribe(new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) throws Exception {
                android.util.Log.e("TAG", "check1: "+Thread.currentThread().getName()+" "+printHex(bytes) );
            }
        });
    }


    public static String printHex(byte[] command) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < command.length; i++) {
            byte b = command[i];
            int i1 = byteToInt(b);
            String s = Integer.toHexString(i1);
            if (s.length() == 1) {
                s = "0" + s;
            }
            buffer.append(s);
        }
        return buffer.toString();
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }
}
