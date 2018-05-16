package com.cabinet.command.driver.worker;


import android.os.Handler;

import com.cabinet.command.driver.worker.abs.IHandler;

import static com.cabinet.command.driver.worker.WorkRunnable.DELAYED_TIME;

/**
 * Description : 工作线程的handler
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WorkerHandler extends Handler implements IHandler {

    private WorkRunnable runnable = new WorkRunnable();

    private boolean isDelayed;

    public WorkerHandler() {
        super(new WorkerHandlerCallback());
    }

    /**
     * 执行任务
     */
    @Override
    public void run() {
        isDelayed = false;
        postDelayed(runnable, DELAYED_TIME);
    }

    /**
     * 延时执行
     *
     * @param time
     */
    @Override
    public void runDelayed(long time) {
        isDelayed = true;
        postDelayed(runnable, time);
    }

    @Override
    public boolean isDelayed() {
        return isDelayed;
    }

}
