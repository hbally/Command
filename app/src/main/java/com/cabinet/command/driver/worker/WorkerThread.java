package com.cabinet.command.driver.worker;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cabinet.command.driver.command.Command;
import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.manager.Manager;
import com.cabinet.command.driver.worker.abs.IWorker;

import java.util.ArrayList;
import java.util.List;

import static com.cabinet.command.driver.worker.WorkerHandlerCallback.*;

/**
 * Description : 工作线程
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WorkerThread extends Thread implements IWorker {
    private static final String TAG = "WorkerThread";

    private WorkerHandler workerHandler;
    private List<Command> commands;

    public WorkerThread() {
        super(TAG);
    }

    /**
     * 执行任务
     */
    @Override
    public void run() {
        Looper.prepare();
        commands = new ArrayList<>();
        workerHandler = new WorkerHandler();
        workerHandler.run();
        Looper.loop();
    }

    /**
     * 获取工作线程的handler
     */
    @Override
    public WorkerHandler handler() {
        return workerHandler;
    }

    /**
     * 所有拦截器
     */
    @Override
    public List<Command> commands() {
        return commands;
    }

    /**
     * 添加
     */
    @Override
    public void addCommand(Command command) {
        if (!Manager.getInstance().isOpen()) {
            Log.e(TAG, "drive does not open ! 串口未打开");
            command.observer.onNext(new Result(false).setMsg("串口未打开"));
            return;
        }
        Message message = workerHandler.obtainMessage();
        message.what = ADD_COMMAND;
        message.obj = command;
        workerHandler.sendMessage(message);
    }

    /**
     * 移除
     */
    @Override
    public void removeCommand(Command command) {
        if (!Manager.getInstance().isOpen()) {
            Log.e(TAG, "drive does not open ! 串口未打开");
            command.observer.onNext(new Result(false).setMsg("串口未打开"));
            return;
        }
        Message message = workerHandler.obtainMessage();
        message.what = REMOVE_COMMAND;
        message.obj = command;
        workerHandler.sendMessage(message);
    }

    /**
     * 清除
     */
    @Override
    public void clear() {
        if (!Manager.getInstance().isOpen()) {
            Log.e(TAG, "drive does not open ! 串口未打开");
            return;
        }
        workerHandler.sendEmptyMessage(CLEAR_COMMAND);
    }

    /**
     * 工作
     */
    @Override
    public void work() {
        if (!isAlive()) {
            start();
        }
    }
}
