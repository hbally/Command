package com.cabinet.command.driver.worker;

import java.util.Arrays;
import java.util.Queue;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Buffer {

    private static final int BUFFER_SIZE = 100;
    private static byte[] sbuff = new byte[BUFFER_SIZE];
//    private static final int POOL_SIZE = 3;
//
//    // 缓冲池
//    private static byte[][] sBufferPool = new byte[POOL_SIZE][];
//
//    private static int sIndex = 0;
//
//    static {
//        for (int i = 0; i < 4; i++) {
//            sBufferPool[i] = new byte[BUFFER_SIZE];
//        }
//    }

    public static byte[] obtainBuffer() {

//        synchronized (Buffer.class) {
//            if (sIndex < POOL_SIZE) {
//                // 从池中取
//                byte[] buff = sBufferPool[sIndex];
//                sIndex++;
//                // 重置
//                Arrays.fill(buff, (byte) 0);
//
//                return buff;
//            }
//        }
//        sIndex = sIndex % POOL_SIZE;
        Arrays.fill(sbuff, (byte) 0);
        // 池中没有就初始化一个
        return sbuff;
    }


}
