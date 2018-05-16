package com.cabinet.command.crc.intercenter;


public interface InterceptorCallBack {

    //拦截成功
    void interceptor(byte[] command);

    //握手成功
    void handshakeSuccess(byte[] command);

    //握手失败
    void handshakeFailed(byte[] command);

    //超时
    void timeOut(byte[] command);

    //单片机要处理的信息
    void logInfo(byte[] command);

    //拦截失败
    void onSendFailed(byte[] command);


}
