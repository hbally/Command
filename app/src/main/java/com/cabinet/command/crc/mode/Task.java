package com.cabinet.command.crc.mode;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cabinet.command.crc.DriverManager;
import com.cabinet.command.utils.CRC16X25Util;
import com.cabinet.command.utils.HexUtil;
import com.cabinet.command.utils.StringUtils;
import com.cabinet.command.crc.intercenter.Interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cabinet.command.crc.mode.WhatConfig.*;
import static com.cabinet.command.utils.SingleChipConstant.*;
import static com.cabinet.command.crc.DriverManager.TAG;

/**
 * Description : 执行读写任务
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/4
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Task implements Runnable, Handler.Callback {

    //数据长度位置
    public static final int DATA_LENGTH_INDEX = HEAD_SIZE + ADDRESS_SIZE + FUNCTION_CODE_SIZE + LOG_LENGTH;
    //数据的基础长度值
    private static final int PACKAGE_BASE_LENGTH = DATA_LENGTH_INDEX + DATA_LENGTH + END_SIZE;
    private static final int READ_BYTE_SIZE = 100;

    //每一次读取多留出来的数据整备下一次整合 在判断
    private static byte[] sPartData;

    /**
     * 不断进行读取操作
     */
    @Override
    public void run() {

        byte[] command = new byte[READ_BYTE_SIZE];
        int length = DriverManager.getInstance()
                .driver().ReadData(command, READ_BYTE_SIZE);
        if (length > 0) {
            //截取数组有效长度
            command = Arrays.copyOfRange(command, 0, length);
            if (command.length > PACKAGE_BASE_LENGTH) {
                if (sPartData != null) {
                    command = CRC16X25Util.concatAll(sPartData, command);
                    sPartData = null;
                }
                handleCommand(command);
            }
        }
        DriverManager.getInstance().runTask();
    }


    /**
     * 处理读取到的数据
     *
     * @param command 获取的byte数组
     */
    private synchronized void handleCommand(byte[] command) {

        Log.e(TAG, "disposeData 接收到的数据" + Arrays.toString(command));

        // 不是一个合法的包
        if (command.length < PACKAGE_BASE_LENGTH) return;

        // 表示数据有问题 包头不对
        if (command[0] != -1 || command[1] == -2) {
            // 舍弃第一位
            command = Arrays.copyOfRange(command, 1, command.length);
            // 重新判断
            handleCommand(command);
        }

        byte[] len = Arrays.copyOfRange(command, DATA_LENGTH_INDEX, DATA_LENGTH_INDEX + DATA_LENGTH);
        //补成四个字节的数据
        byte[] int_len = new byte[]{0, 0, 0, len[0]};

        // 拿到数据的长
        int dataLength = HexUtil.byteArrayToInt(int_len);

        //如果这组数据等于不够一个包长 无效
        if (dataLength < PACKAGE_BASE_LENGTH) {
            // 拼接给下次用
            sPartData = command;
            return;
        }

        // 加上crc校验的两位
        dataLength += 2;

        // 截取此包
        byte[] packageData = Arrays.copyOfRange(command, 0, dataLength);

        // 判断包尾
        if (packageData[dataLength - 3] != -1 || packageData[dataLength - 4] != -2) {
            // 包尾不对拼接上来后下次重新执行
            sPartData = command;
            return;
        }

        // 数据正常的情况下
        //CRC校验部分
        boolean passCRC = CRC16X25Util.isPassCRC(packageData, dataLength);

        // 成功
        if (passCRC) {
            circulationData(packageData);//校验通过
        }
        StringUtils.printHex(packageData, "校验数据:" + passCRC);

        // 表示还够一个包
        if (command.length > dataLength + PACKAGE_BASE_LENGTH) {
            command = Arrays.copyOfRange(command, dataLength, command.length);
            // 进行下一个包的处理
            handleCommand(command);
        }

        // 不够一个包
        sPartData = command;
    }

    /**
     * 处理接收到的数据
     */
    private void circulationData(byte[] packageData) {
        List<Interceptor> removeAll = new ArrayList<>();
        List<Interceptor> interceptors = DriverManager.getInstance().getInterceptors();
        //实行拦击功能
        for (int i = 0; i < interceptors.size(); i++) {
            Interceptor interceptor = interceptors.get(i);
            if (interceptor.interceptor(packageData)) {
                //如果是的情况
                removeAll.add(interceptor);
                break;
            }
        }

        if (removeAll.size() > 0) {
            interceptors.removeAll(removeAll);
            for (int i = 0; i < removeAll.size(); i++) {
                Interceptor interceptor = removeAll.get(i);
                Log.e(TAG, "删除数据:" + Arrays.toString(interceptor.getCommand()));
                interceptor.cancel();
                interceptor.getCallBack().interceptor(packageData);
            }
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ADD_INTERCEPTOR:
                add(msg);
                break;
            case REMOVE_INTERCEPTOR:

                break;
            case REMOVE_INTERCEPTOR_ALL:

                break;
            case READ_DATA:

                break;

        }

        // 不吃掉handler本身的事件 可以留一个代理位置
        return false;
    }


    /**
     * 往串口池写数据
     */
    private void add(Message msg) {
        Command command = (Command) msg.obj;
        if (command.interceptor != null) {
            // 添加拦截器
            DriverManager.getInstance().getInterceptors().add(command.interceptor);
        }
        // 发送指令
        byte[] sCommand = command.command;
        int real = DriverManager.getInstance().driver().WriteData(sCommand, sCommand.length);

        if (command.interceptor!=null){
            // 存在拦截器就移除
            DriverManager.getInstance().getInterceptors().remove(command.interceptor);
        }

        // 失败
        if (real <= 0 ) {
            // 发送失败
            StringUtils.printHex(sCommand, "发送数据(十六进制)失败:");
            // 失败回调
            command.interceptor.getCallBack().onSendFailed(sCommand);
            return;
        }

        StringUtils.printHex(sCommand, "发送数据(十六进制)成功:");
    }


}
