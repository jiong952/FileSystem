package com.zjh.service;

/**
 * @author 张俊鸿
 * @description: 磁盘操作
 * @since 2022-06-30 14:10
 */
public interface DiskService {
    /**
     * 从FAT表往下一直找文件所属块
     * 读取磁盘内容
     *
     * @param firstId 第一块号
     * @return {@link String}
     */
    String getContent(int firstId);

    /**
     * 文件内容写入磁盘
     *
     * @param content 文件内容
     * @return int 返回第一块的磁盘号
     */
    int writeToDisk(String content);

}
