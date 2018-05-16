package com.cabinet.command.utils;


import java.util.Arrays;

import static com.cabinet.command.utils.SingleChipConstant.*;


/**
 * 组装命令
 */
public class CommandAssemble {
    public static final int IS_LOG = 0;
    public static final int NOT_LOG = 1;

    /**
     * 结束
     * 组装一个指令
     *
     * @param addressNumber  地址
     * @param functionNumber 功能
     * @param dataNumber     数据
     * @param dataByteSize   数据大小
     * @param log            是否是日志
     * @return
     */
    public static byte[] assembleCommand(int addressNumber, int functionNumber, int log, int dataNumber, int dataByteSize) {
        //数据
        byte[] data_byte = HexUtil.intToByteArray(dataNumber);
        data_byte = Arrays.copyOfRange(data_byte, data_byte.length - dataByteSize, data_byte.length);
        return assembleCommand(addressNumber, functionNumber, data_byte);
    }


    public static byte[] assembleCommand(int addressNumber, int functionNumber, byte[] data_byte) {
        return assembleCommand(addressNumber, functionNumber,NOT_LOG, data_byte);
    }


    /**
     * 结束
     * 组装一个指令
     *
     * @param addressNumber  地址
     * @param functionNumber 功能
     * @param log            是否是日志
     * @return
     */
    public static byte[] assembleCommand(int addressNumber, int functionNumber, int log, byte[] data) {
        //包头大小
        byte[] head = null;
        //地址大小
        byte[] address = null;
        //功能码大小
        byte[] functionCode = null;
        //是否是日志
        byte[] logCode = null;
        //包的长度 不加校验码
        byte[] length = new byte[DATA_LENGTH];
        //包尾
        byte[] end = null;

        //赋值头部信息
        byte[] data_head = HexUtil.intToByteArray(DATA_HEAD);
        int head_length = data_head.length > HEAD_SIZE ? HEAD_SIZE : data_head.length;
        head = Arrays.copyOfRange(data_head, data_head.length - head_length, data_head.length);

        //地址信息赋值
        byte[] address_byte = HexUtil.intToByteArray(addressNumber);
        int address_length = address_byte.length > ADDRESS_SIZE ? ADDRESS_SIZE : address_byte.length;
        address = Arrays.copyOfRange(address_byte, address_byte.length - address_length, address_byte.length);

        //功能码
        byte[] function_byte = HexUtil.intToByteArray(functionNumber);
        int function_length = function_byte.length > FUNCTION_CODE_SIZE ? FUNCTION_CODE_SIZE : function_byte.length;
        functionCode = Arrays.copyOfRange(function_byte, function_byte.length - function_length, function_byte.length);

        //是否是日志
        byte[] log_byte = HexUtil.intToByteArray(log);
        logCode = Arrays.copyOfRange(log_byte, log_byte.length - 1, log_byte.length);

        //包尾
        byte[] end_byte = HexUtil.intToByteArray(DATA_END);
        end = Arrays.copyOfRange(end_byte, end_byte.length - END_SIZE, end_byte.length);

        //长度
        byte[] length_byte = HexUtil.intToByteArray(CRC16X25Util.concatAll(head, address, functionCode, logCode, length, data, end).length);
        int length_length = length_byte.length > DATA_LENGTH ? DATA_LENGTH : length_byte.length;
        length = Arrays.copyOfRange(length_byte, length_byte.length - length_length, length_byte.length);

        //组装成一个数组
        byte[] submit = CRC16X25Util.concatAll(head, address, functionCode, logCode, length, data, end);

        //开始CRC校验 并且添加在最后两位
        submit = CRC16X25Util.setParamCRC(submit);

        return submit;
    }


    /**
     * 结束
     * 组装一个指令
     *
     * @param addressNumber  地址
     * @param functionNumber 功能
     * @param dataNumber     数据
     * @param dataByteSize   数据大小
     * @return
     */
    public static byte[] assembleCommand(int addressNumber, int functionNumber, int dataNumber, int dataByteSize) {
        return assembleCommand(addressNumber, functionNumber, NOT_LOG, dataNumber, dataByteSize);
    }

    /**
     * 固件版本上报
     *
     * @param addressNumber
     * @param functionNumber
     * @param version
     * @return
     */
    public static byte[] assembleCommandUpgrade(int addressNumber, int functionNumber, int version) {


        return assembleCommandUpgrade(addressNumber, functionNumber, version, -1);
    }

    /**
     * 固件升级命令
     *
     * @return
     */
    public static byte[] assembleCommandUpgrade(int addressNumber, int functionNumber, int version, int allCount) {
        //包头大小
        byte[] head = null;
        //地址大小
        byte[] address = null;
        //功能码大小
        byte[] functionCode = null;
        //包尾
        byte[] end = null;
        // 版本号
        byte[] versionCode = null;
        // 总块数
        byte[] allCountSize = null;


        //赋值头部信息
        byte[] data_head = HexUtil.intToByteArray(DATA_HEAD_UPDATA);
        int head_length = data_head.length > HEAD_SIZE ? HEAD_SIZE : data_head.length;
        head = Arrays.copyOfRange(data_head, data_head.length - head_length, data_head.length);

        //地址信息赋值
        byte[] address_byte = HexUtil.intToByteArray(addressNumber);
        int address_length = address_byte.length > ADDRESS_SIZE ? ADDRESS_SIZE : address_byte.length;
        address = Arrays.copyOfRange(address_byte, address_byte.length - address_length, address_byte.length);

        //功能码
        byte[] function_byte = HexUtil.intToByteArray(functionNumber);
        int function_length = function_byte.length > FUNCTION_CODE_SIZE ? FUNCTION_CODE_SIZE : function_byte.length;
        functionCode = Arrays.copyOfRange(function_byte, function_byte.length - function_length, function_byte.length);

        // 版本号
        versionCode = HexUtil.intToByteArray(version);
        int version_code_size = versionCode.length > VERSION_CODE_SIZE ? VERSION_CODE_SIZE : versionCode.length;
        versionCode = Arrays.copyOfRange(versionCode, versionCode.length - version_code_size, versionCode.length);

        // 总大小
        if (allCount != -1) {
            allCountSize = HexUtil.intToByteArray(allCount);
            int all_count_length = allCountSize.length > ALL_COUNT_SIZE ? ALL_COUNT_SIZE : allCountSize.length;
            allCountSize = Arrays.copyOfRange(allCountSize, allCountSize.length - all_count_length, allCountSize.length);
        }

        //包尾
        byte[] end_byte = HexUtil.intToByteArray(DATA_END_UPDATA);
        end = Arrays.copyOfRange(end_byte, end_byte.length - END_SIZE, end_byte.length);


        //组装成一个数组
        byte[] submit = allCount != -1 ? CRC16X25Util.concatAll(head, address, functionCode, versionCode, allCountSize, end) : CRC16X25Util.concatAll(head, address, functionCode, versionCode, end);

        //开始CRC校验 并且添加在最后两位
        submit = CRC16X25Util.setParamCRC(submit);

        return submit;
    }

    /**
     * 升级指令
     *
     * @param version 版本号
     * @param current 当前块
     * @return
     */
    public static byte[] requestUpgradeCommand(int adress, int version, int current) {
        return requestUpgradeCommand(adress, version, current, null);
    }


    /**
     * 升级指令
     *
     * @param version 版本号
     * @param current 当前块
     * @param data    数据
     * @return
     */
    public static byte[] requestUpgradeCommand(int adress, int version, int current, byte[] data) {
        //赋值头部信息
        byte[] data_head = HexUtil.intToByteArray(DATA_HEAD_UPDATA);
        int head_length = data_head.length > HEAD_SIZE ? HEAD_SIZE : data_head.length;
        byte[] head = Arrays.copyOfRange(data_head, data_head.length - head_length, data_head.length);

        //地址信息赋值
        byte[] address_byte = HexUtil.intToByteArray(adress);
        int address_length = address_byte.length > ADDRESS_SIZE ? ADDRESS_SIZE : address_byte.length;
        byte[] address = Arrays.copyOfRange(address_byte, address_byte.length - address_length, address_byte.length);

        //功能码
        byte[] function_byte = HexUtil.intToByteArray(0x03);
        int function_length = function_byte.length > FUNCTION_CODE_SIZE ? FUNCTION_CODE_SIZE : function_byte.length;
        byte[] functionCode = Arrays.copyOfRange(function_byte, function_byte.length - function_length, function_byte.length);

        // 版本号
        byte[] versionC_byte = HexUtil.intToByteArray(version);
        int version_byte_size = versionC_byte.length > VERSION_CODE_SIZE ? VERSION_CODE_SIZE : versionC_byte.length;
        byte[] versionCode = Arrays.copyOfRange(versionC_byte, versionC_byte.length - version_byte_size, versionC_byte.length);

        // 当前块
        byte[] current_byte = HexUtil.intToByteArray(current);
        int current_byte_size = current_byte.length > CURRENT_CODE_SIZE ? CURRENT_CODE_SIZE : current_byte.length;
        byte[] currentCode = Arrays.copyOfRange(current_byte, current_byte.length - current_byte_size, current_byte.length);

        //包尾
        byte[] end_byte = HexUtil.intToByteArray(DATA_END_UPDATA);
        byte[] end = Arrays.copyOfRange(end_byte, end_byte.length - END_SIZE, end_byte.length);

        //组装成一个数组
        byte[] submit = data != null ? CRC16X25Util.concatAll(head, address, functionCode, versionCode, currentCode, data, end) : CRC16X25Util.concatAll(head, address, functionCode, versionCode, currentCode, end);

        //开始CRC校验 并且添加在最后两位
        submit = CRC16X25Util.setParamCRC(submit);

        return submit;
    }


    /**
     * 自检指令
     */
    public static byte[] checkInstructCommand(int adress) {
        //赋值头部 尾部 信息
        byte[] data_head_and_end = HexUtil.intToByteArray(CHECK_DATA_HEAD_AND_END);
        int head_length = data_head_and_end.length > HEAD_SIZE ? HEAD_SIZE : data_head_and_end.length;
        byte[] head_and_end = Arrays.copyOfRange(data_head_and_end, data_head_and_end.length - head_length, data_head_and_end.length);

        //地址信息赋值
        byte[] address_byte = HexUtil.intToByteArray(adress);
        int address_length = address_byte.length > ADDRESS_SIZE ? ADDRESS_SIZE : address_byte.length;
        byte[] address = Arrays.copyOfRange(address_byte, address_byte.length - address_length, address_byte.length);

        //功能码
        byte[] function_byte = HexUtil.intToByteArray(0x04);
        int function_length = function_byte.length > FUNCTION_CODE_SIZE ? FUNCTION_CODE_SIZE : function_byte.length;
        byte[] functionCode = Arrays.copyOfRange(function_byte, function_byte.length - function_length, function_byte.length);

        //长度
        byte[] length_byte = HexUtil.intToByteArray(7);
        int length_length = length_byte.length > DATA_LENGTH ? DATA_LENGTH : length_byte.length;
        byte[] length = Arrays.copyOfRange(length_byte, length_byte.length - length_length, length_byte.length);

        //组装成一个数组
        byte[] submit = CRC16X25Util.concatAll(data_head_and_end, address, functionCode, length, data_head_and_end);

        // crc校验
        return CRC16X25Util.setParamCRC(submit);
    }
}
