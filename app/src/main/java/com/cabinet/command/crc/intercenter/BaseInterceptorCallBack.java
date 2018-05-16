package com.cabinet.command.crc.intercenter;


public abstract class BaseInterceptorCallBack implements InterceptorCallBack {


    @Override
    public void handshakeSuccess(byte[] command) {

    }

    @Override
    public void handshakeFailed(byte[] command) {

    }

    @Override
    public void timeOut(byte[] command) {

    }

    @Override
    public void logInfo(byte[] command) {

    }

    @Override
    public void onSendFailed(byte[] command) {

    }
}
