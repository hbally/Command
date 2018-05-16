package com.cabinet.command.crc;

import android.nfc.Tag;
import android.util.Log;

import com.cabinet.command.crc.intercenter.Interceptor;
import com.cabinet.command.crc.mode.Command;
import com.cabinet.command.crc.mode.DriverHandler;

import static com.cabinet.command.crc.DriverManager.TAG;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class SendUtils {

    public static void sendCommand(Command command) {
        DriverHandler handler = DriverManager.getInstance().getHandler();
        if (handler == null) {
            Log.e(TAG, "SendUtils.sendCommand: 串口是否打开！！！！！");
            return;
        }
        handler.sendCommand(command);
    }
}
