package com.cabinet.command.driver.worker;

import android.os.Handler;
import android.os.Message;

import com.cabinet.command.driver.command.Command;
import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.manager.Manager;
import com.cabinet.command.utils.StringUtils;

import java.util.List;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Description : handler 发送任务后的回调
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WorkerHandlerCallback implements Handler.Callback {

    // 添加指令
    public static final int ADD_COMMAND = 0x01;
    // 移除
    public static final int REMOVE_COMMAND = 0x02;
    // 清空所有的指令
    public static final int CLEAR_COMMAND = 0x03;
    // arg1
    public static final int ARG1_TAG = 0x40;

    private List<Command> commands;
    private CH34xUARTDriver driver;

    public WorkerHandlerCallback() {
        commands = Manager.getInstance().worker().commands();
        driver = Manager.getInstance().driver();
    }


    @Override
    public boolean handleMessage(Message msg) {
        // 获取指令
        Command obj = (Command) msg.obj;
        switch (msg.what) {
            case ADD_COMMAND:
                // 添加
                commands.add(obj);
                // 往串口写数据
                write(obj);
                break;
            case REMOVE_COMMAND:
                remove(msg);
                break;

            case CLEAR_COMMAND:
                // 移除所有的
                commands.clear();
                break;
        }
        return false;
    }

    private void remove(Message msg) {
        Command obj = (Command) msg.obj;

        // 还需要回调error
        if (msg.arg1 == ARG1_TAG) {
            obj.observer.onNext(new Result(false).setMsg("握手失败"));
        }

        // 移除多余的指令
        commands.remove(msg.obj);
    }

    /**
     * 写数据
     */
    private void write(Command command) {
        if (driver != null) {
            // 写入 返回是否成功
            int real = driver.WriteData(command.command, command.command.length);
            // 失败
            if (real <= 0) {
                // 发送失败
                StringUtils.printHex(command.command, "发送数据(十六进制)失败:");
                // 失败回调
                command.observer.onNext(new Result(false).setMsg("发送失败"));
                // 移除掉失败的command
                Manager.getInstance().worker().removeCommand(command);
                return;
            }
            StringUtils.printHex(command.command, "发送数据(十六进制)成功:");
        }
    }
}
