package com.cabinet.command.api;

import org.driver.annoation.Adress;
import org.driver.annoation.End;
import org.driver.annoation.Fun;
import org.driver.annoation.Head;
import org.driver.annoation.Intercept;
import org.driver.annoation.Log;
import org.driver.annoation.Retry;

import io.reactivex.Observable;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface UsbApi {

    /**
     * 发送指令后 拦截新的指令
     */
    @Head(0xfffe)
    @Adress(0x05)
    @Fun(0x06)
    @Log(Log.Logger.ON)
    @Retry(0x05)
    @End(0xfeff)
    @Intercept({-1,-2,10,10,9,10,10,-2,-1})
    Observable<byte[]> check();
}
