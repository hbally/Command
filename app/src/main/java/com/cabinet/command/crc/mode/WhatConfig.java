package com.cabinet.command.crc.mode;

/**
 * Description : what值类型
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface WhatConfig {

    int ADD_INTERCEPTOR = 0x01;
    int REMOVE_INTERCEPTOR = 0x02;
    int REMOVE_INTERCEPTOR_ALL = 0x03;
    int READ_DATA = 0x04;

}
