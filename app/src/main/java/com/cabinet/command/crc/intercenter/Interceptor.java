package com.cabinet.command.crc.intercenter;


import android.os.CountDownTimer;


import java.util.ArrayList;
import java.util.List;

/**
 * 数据拦截
 * 接收数据的方式是不停的去读取
 * 根据类型的不同做相应的操作
 * 但是由于流程的不同不能执行统一的任务
 * 需要任务细化，每一个任务实现独立流程，实现多模块配置化管理
 * 每一个拦截加上超时时间 和握手
 */
public class Interceptor {
    //默认不需要握手
    public static final boolean DEFAULT_HANDSHAKE = false;
    //默认需要日志
    public static final boolean NEED_LOG = false;
    //默认超时时间为20秒
    public static final long DEFAULT_TIMEOUT = /*TimeConstants.SEC * */20*1000;
    //握手超时时间
    public static final long DEFAULT_HANDSHAKE_TIMEOUT = /*TimeConstants.SEC **/ 5 * 100*1000;
    //指令
    private byte[] command;
    //指定回调
    private InterceptorCallBack callBack;
    //倒计时
    private CountDownTimer timer;
    //超时时间
    private long timeOut = DEFAULT_TIMEOUT;
    //是否握手
    private boolean handshake = DEFAULT_HANDSHAKE;
    //是否握手过
    private boolean isHandshake = false;
    //是否需要日志 默认需要日志
    private boolean needLog = NEED_LOG;

    private long handshakeTimeOut = DEFAULT_HANDSHAKE_TIMEOUT;

    private boolean cancel;
    //额外的日志拦截
    private List<byte[]> logInterceptor = new ArrayList<>();

    public Interceptor() {
        // Do nothing
    }

    public Interceptor(byte[] command) {
        this.command = command;
    }

    /**
     * @param command  指令
     * @param callBack 拦截回调
     */
    public Interceptor(byte[] command, InterceptorCallBack callBack) {
        this(command);
        this.callBack = callBack;
    }

    /**
     * @param command  指令
     * @param timeOut  超时时间
     * @param callBack 拦截回调
     */
    public Interceptor(byte[] command, long timeOut, InterceptorCallBack callBack) {
        this(command, callBack);
        this.timeOut = timeOut;
    }

    /**
     * @param command   指令
     * @param timeOut   超时时间
     * @param handshake 是否需要握手
     * @param callBack  拦截回调
     */
    public Interceptor(byte[] command, long timeOut, boolean handshake, InterceptorCallBack callBack) {
        this(command, timeOut, callBack);
        this.handshake = handshake;
    }

    /**
     * @param command   指令
     * @param timeOut   超时时间
     * @param needLog   是否需要日志
     * @param handshake 是否需要握手
     * @param callBack  拦截回调
     */
    public Interceptor(byte[] command, long timeOut, boolean needLog, boolean handshake, InterceptorCallBack callBack) {
        this(command, timeOut, handshake, callBack);
        this.needLog = needLog;
    }

    /**
     * 拦截
     *
     * @param data
     * @return
     */
    public boolean interceptor(byte[] data) {
        boolean ren = false;
        if (command == null) {
            ren = true;
        }
//        if (data[3] == command[3]//地址
//                && data[2] == command[2]) {//[T][T][ADD][F]
//            if (HexUtil.memcmp(data, command) && handshake && !isHandshake) {
//                //返回指令完全一样表示握手成功
//                isHandshake = true;
//                callBack.handshakeSuccess(data);
//            } else if (StringUtils.judgeLog(data, command, logInterceptor) && needLog) {
//                //插入日志
//                callBack.logInfo(data);
//            } else if (data[3] == command[3]//地址
//                    && data[2] == command[2]) {//功能码
//                ren = true;
//            }
//        }
        return ren;
    }


    /**
     * 设置Time Out
     */
    public void setCountDown() {
        setCountDown(timeOut);
    }

    /**
     * 设置Time Out
     */
    public void setCountDown(long timeOut) {
        if (timeOut != 0) {
//            timer = new CountDownTimer(timeOut, 100) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    if (handshake && millisUntilFinished >= handshakeTimeOut && !isHandshake) {
//                        //如果没有握手成功则失败
//                        Receiver.getInstance().removeInterceptor(Interceptor.this);
//                        callBack.handshakeFailed(Interceptor.this.command);
//                    }
//                }
//
//                @Override
//                public void onFinish() {
//                    //删除拦截
//                    if (!cancel) {
//                        Receiver.getInstance().removeInterceptor(Interceptor.this);
//                        callBack.timeOut(Interceptor.this.command);
//                    }
//                }
//            };
            timer.start();
        }
    }

    /**
     * 取消拦截
     */
    public void cancel() {
        cancel = true;
        if (timer != null) {
            //删除拦截
            timer.cancel();
            timer = null;
        }
    }

    public void addLogInterceptor(byte[] command) {
        logInterceptor.add(command);
    }

    public byte[] getCommand() {
        return command;
    }

    public void setCommand(byte[] command) {
        this.command = command;
    }

    public InterceptorCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(InterceptorCallBack callBack) {
        this.callBack = callBack;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isHandshake() {
        return handshake;
    }

    public void setHandshake(boolean handshake) {
        this.handshake = handshake;
    }

    public boolean isNeedLog() {
        return needLog;
    }

    public void setNeedLog(boolean needLog) {
        this.needLog = needLog;
    }

    public long getHandshakeTimeOut() {
        return handshakeTimeOut;
    }

    public void setHandshakeTimeOut(long handshakeTimeOut) {
        this.handshakeTimeOut = handshakeTimeOut;
    }
}
