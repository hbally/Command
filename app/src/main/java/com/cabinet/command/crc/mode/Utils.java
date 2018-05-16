package com.cabinet.command.crc.mode;

import android.util.Log;

import com.cabinet.command.crc.DriverManager;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

import static com.cabinet.command.crc.DriverManager.TAG;

/**
 * Description : 打开驱动的一个操作
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/4
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Utils {

    public static boolean openDriver(DriveQuest quest) {
        CH34xUARTDriver driver = DriverManager.getInstance().driver();
        // ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
        int rectal = driver.ResumeUsbList();

        if (rectal == -1) {
            Log.e(TAG, "Utils.openDriver 打开设备失败!\n");
            return false;
        }
        if (rectal != 0) {
            Log.e(TAG, "Utils.openDriver 没有发现设备，请检查你的设备是否正常\n");
            return false;
        }

        //对串口设备进行初始化操作
        boolean uartInit = driver.UartInit();

        if (!uartInit) {
            Log.e(TAG, "Utils.openDriver 设备初始化失败!\n");
            return false;
        }
        Log.e(TAG, "Utils.openDriver 打开设备成功!\n");
        //配置串口波特率
        boolean config = driver.SetConfig(quest.baudRate(), quest.dataBit(), quest.stopBit(), quest.parity(), quest.flowControl());

        if (config) {
            Log.e(TAG, "Utils.openDriver 串口设置成功!\n");
            return true;
        }

        Log.e(TAG, "Utils.openDriver 串口设置失败!\n");
        return false;
    }
}
