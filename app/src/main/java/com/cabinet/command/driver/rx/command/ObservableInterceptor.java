package com.cabinet.command.driver.rx.command;

import com.cabinet.command.driver.command.Command;
import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.manager.Manager;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Description : 拦截指令
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public final class ObservableInterceptor extends Observable<Result> {

    private byte[] interceptor;

    public ObservableInterceptor(byte[] interceptor) {
        this.interceptor = interceptor;
    }


    @Override
    protected void subscribeActual(Observer<? super Result> observer) {

        Command command = new Command(null,interceptor,new InterceptorObserver(observer));
        // 添加到工作线程
        Manager.getInstance().worker().addCommand(command);
    }


    final class InterceptorObserver implements Observer<Result>{

        Observer<? super Result> observer;

        public InterceptorObserver(Observer<? super Result> observer) {
            this.observer = observer;
        }


        @Override
        public void onSubscribe(Disposable d) {
            observer.onSubscribe(d);
        }


        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }


        @Override
        public void onComplete() {
            observer.onComplete();
        }

        @Override
        public void onNext(Result o) {
            observer.onNext(o);
        }
    }
}
