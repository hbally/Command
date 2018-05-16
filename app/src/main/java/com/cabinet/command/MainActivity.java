package com.cabinet.command;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rx.driver(getApplication())
                .flatMap(new Function<Boolean, ObservableSource<Result>>() {
                    @Override
                    public ObservableSource<Result> apply(Boolean aBoolean) throws Exception {
                        Toast.makeText(MainActivity.this, "" + aBoolean, Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "accept: " + aBoolean);
                        byte[] command = CommandAssemble.assembleCommand(0x05, 0x01, 1, 1);
                        return Rx.send(command);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        Log.e("TAG", "accept: " + result.isSuccess() + result.getMsg());
                        StringUtils.printHex(result.getResult());
                    }
                });

//        new Retrofit.Builder()
//                .addCallAdapterFactory()
//                .build().create()

    }

    public void click(View view) {
        
    }
}
