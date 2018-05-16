package com.cabinet.command.crc.observer;

import com.cabinet.command.crc.SendUtils;
import com.cabinet.command.crc.intercenter.BaseInterceptorCallBack;
import com.cabinet.command.crc.intercenter.Interceptor;
import com.cabinet.command.crc.mode.Command;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public final class ObservableSend extends Observable<Byte[]> {

    private Command mCommand;

    private ObservableSend(Command command) {
        this.mCommand = command;
    }

    public static Observable<Byte[]> send(Command command) {
        return new ObservableSend(command);
    }

    @Override
    protected void subscribeActual(final Observer<? super Byte[]> observer) {

        mCommand.interceptor.setCallBack(new BaseInterceptorCallBack() {
            @Override
            public void interceptor(byte[] command) {
                subscribe(new SendObserver(observer));
            }
        });

        SendUtils.sendCommand(mCommand);
    }


    final class SendObserver implements Observer<Byte[]> {

        private Observer<? super Byte[]> observer;

        SendObserver(Observer<? super Byte[]> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe(Disposable d) {
            observer.onSubscribe(d);
        }

        @Override
        public void onNext(Byte[] t) {
            observer.onNext(t);
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
