package com.cabinet.command.driver.worker.abs;

import com.cabinet.command.driver.command.Command;
import com.cabinet.command.driver.worker.WorkerHandler;

import java.util.List;

/**
 * Description : 工作接口
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface IWorker {

    /**
     * 获取工作线程的handler
     */
    WorkerHandler handler();

    /**
     * 所有拦截器
     */
    List<Command> commands();

    /**
     * 添加
     */
    void addCommand(Command command);

    /**
     * 移除
     */
    void removeCommand(Command command);

    /**
     * 清除
     */
    void clear();

    /**
     * 工作
     */
    void work();
}
