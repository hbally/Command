package com.cabinet.command.driver.worker;

import android.util.Log;

import com.cabinet.command.crc.DriverManager;
import com.cabinet.command.driver.command.Command;
import com.cabinet.command.driver.command.Result;
import com.cabinet.command.driver.manager.Manager;
import com.cabinet.command.utils.CRC16X25Util;
import com.cabinet.command.utils.HexUtil;
import com.cabinet.command.utils.StringUtils;

import java.util.Arrays;

import static com.cabinet.command.crc.DriverManager.TAG;
import static com.cabinet.command.utils.SingleChipConstant.*;

/**
 * Description : 读写执行体 通过 handler.post() 发送
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WorkRunnable implements Runnable {
    /**
     * 延时任务时间
     */
    public static final long DELAYED_TIME = 200;

    //数据长度位置
    public static final int DATA_LENGTH_INDEX = HEAD_SIZE + ADDRESS_SIZE + FUNCTION_CODE_SIZE + LOG_LENGTH;
    //数据的基础长度值
    private static final int PACKAGE_BASE_LENGTH = DATA_LENGTH_INDEX + DATA_LENGTH + END_SIZE;
    private static final int READ_BYTE_SIZE = 100;

    //每一次读取多留出来的数据整备下一次整合 在判断
    private static byte[] sPartData;

    @Override
    public void run() {
        // 初始化一个buffer
        byte[] command = new byte[100];
        int length =Manager.getInstance().driver().ReadData(command, READ_BYTE_SIZE);
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

        handTask();
    }

    /**
     * 处理读取到的数据
     *
     * @param command 获取的byte数组
     */
    private synchronized void handleCommand(byte[] command) {

        Log.e(TAG, "disposeData 接收到的数据" + Arrays.toString(command));

        // 不是一个合法的包 或者不是一个完整的包
        if (command.length < PACKAGE_BASE_LENGTH) {
            sPartData = command;
            return;
        }

        // 表示数据有问题 包头不对
        if (command[0] != -1 || command[1] != -2) {
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

        // 数据正常的情况下 长度减去两位
        //CRC校验部分
        boolean passCRC = CRC16X25Util.isPassCRC(packageData, dataLength-2);

        // 成功
        if (passCRC) {
            circulation(packageData);//校验通过
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
    private void circulation(byte[] packageData) {

        // 遍历拦截器
        for (Command command : Manager.getInstance().worker().commands()) {

                // 包头包尾一致 如果地址一样则指令可以过
                if (command.commandInterceptor[3] == packageData[3]) {

                    // 回调指令
                    command.observer.onNext(new Result(packageData));
                }

        }
    }


    /**
     * 处理循环任务
     */
    private void handTask() {
        WorkerHandler handler = Manager.getInstance().worker().handler();
        if (handler.isDelayed()) {
            handler.runDelayed(DELAYED_TIME);
            return;
        }
        handler.run();
    }
}
