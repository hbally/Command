package com.cabinet.command.driver.rx.command;


import android.os.Message;

import com.cabinet.command.driver.command.Command;
import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.manager.Manager;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.cabinet.command.driver.worker.WorkerHandlerCallback.*;

/**
 * Description : 发送指令
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public final class ObservableSend extends Observable<Result> {

    //握手超时时间
    private static final long DEFAULT_HANDSHAKE_TIMEOUT = 1000 * 5 * 100;

    private byte[] command;
    private byte[] interceptor;
    private Message message;


    public ObservableSend(byte[] command, byte[] interceptor) {
        this.command = command;
        this.interceptor = interceptor;
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
    protected void subscribeActual(Observer<? super Result> observer) {

        // 生成一个指令任务
        Command com = new Command(command, interceptor, new SendObserver(observer));

        // 添加到工作线程
        Manager.getInstance().worker().addCommand(com);

        // 打开才判断
        if (Manager.getInstance().isOpen()) {
            // 握手等待
            message = Message.obtain();
            message.what = REMOVE_COMMAND;
            message.obj = com;
            message.arg1 = ARG1_TAG;
            Manager.getInstance().worker().handler().sendMessageDelayed(message,
                    DEFAULT_HANDSHAKE_TIMEOUT);
        }

    }


    /**
     * 发送任务回调
     */
    final class SendObserver implements Observer<Result> {

        private Observer<? super Result> observer;

        public SendObserver(Observer<? super Result> observer) {
            this.observer = observer;
        }


        @Override
        public void onSubscribe(Disposable d) {
            observer.onSubscribe(d);
        }


        @Override
        public void onNext(Result t) {

            // 失败直接回调
            // 如果有指令 且指令完全一样则表示为握手 并且握手成功
            if (t.isSuccess() && equals(command,t.getResult())) {
                // 握手成功移除这个倒计时任务
                Manager.getInstance().worker().handler().removeMessages(message.what, message.obj);
                return;
            }

            // 回调非握手数据
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

        private boolean equals(byte[] bytes1, byte[] bytes2) {
            if (bytes1 == bytes2) return true;
            if (bytes1.length != bytes2.length) return false;

            for (int i = 0; i < bytes1.length; i++) {
                if (bytes1[i] != bytes2[i]) {
                    return false;
                }
            }
            return true;
        }
    }

}
