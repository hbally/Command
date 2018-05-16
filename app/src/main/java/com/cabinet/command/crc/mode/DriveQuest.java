package com.cabinet.command.crc.mode;

import android.app.Application;

import com.cabinet.command.crc.intercenter.ComConfig;

/**
 * Description : 初始化参数类
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/4
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class DriveQuest {

    private Build P;

    private DriveQuest(Build build) {
        P = build;
    }

    public Build newBuild(){
        return new Build(P);
    }

    public String usbPermission() {
        return P.action_usb_permission;
    }

    public Application app() {
        return P.app;
    }

    public int baudRate() {
        return P.baud_rate;
    }

    public byte dataBit() {
        return P.data_bit;
    }

    public byte parity() {
        return P.parity;
    }

    public byte stopBit() {
        return P.stop_bit;
    }

    public byte flowControl() {
        return P.flow_control;
    }


    public static class Build {

        private Application app;
        private String action_usb_permission = "cn.wch.wchusbdriver.USB_PERMISSION";
        private int baud_rate = ComConfig.Backdates.BACKDATE_115200.getValue();
        private byte data_bit = (byte) ComConfig.DataBits.DataBits_8.getValue();
        private byte stop_bit = (byte) ComConfig.StopBits.SHOPLIFTS_1.getValue();
        private byte parity = (byte) ComConfig.Parity.PARITY_NONE_NONE.getValue();
        private byte flow_control = (byte) ComConfig.FlowControl.FLOW_CONTROL_NONE.getValue();

        public Build(Application app) {
            this.app = app;
        }

        private Build(Build build){
            action_usb_permission = build.action_usb_permission;
            baud_rate = build.baud_rate;
            data_bit = build.data_bit;
            stop_bit = build.stop_bit;
            parity = build.parity;
            flow_control = build.flow_control;
            app = build.app;
        }

        public Build usbPermission(String permission) {
            action_usb_permission = permission;
            return this;
        }

        public Build baudRate(int rate) {
            baud_rate = rate;
            return this;
        }

        public Build dataBit(byte databit) {
            data_bit = databit;
            return this;
        }

        public Build parity(byte parity) {
            this.parity = parity;
            return this;
        }

        public Build stopBit(byte stopbit) {
            stop_bit = stopbit;
            return this;
        }

        public Build flowControl(byte flowcontrol) {
            flow_control = flowcontrol;
            return this;
        }

        public DriveQuest build() {
            return new DriveQuest(this);
        }

    }


}
