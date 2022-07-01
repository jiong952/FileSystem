package com.zjh.service;

import com.zjh.pojo.FCB;

/**
 * @author 张俊鸿
 * @description: 磁盘操作
 * @since 2022-06-30 14:10
 */
public interface DiskService {
    /**
     * 释放目录中所有文件占用内存（后序遍历）
     *
     * @param dirName 目录名
     * @return {@link Boolean}
     */
    Boolean freeDir(String dirName);
    /**
     * 释放文件空间 修改FAT表和位示图
     *
     * @param fcb FCB
     * @return {@link Boolean}
     */
    Boolean freeFile(FCB fcb);

    /**
     * 写入磁盘
     * 文件内容写入磁盘
     *
     * @param content   文件内容
     * @return int 返回第一块的磁盘号
     */
    int writeToDisk(String content);

    /**
     * 从头寻找空闲块
     *
     * @return int 块号
     */
    int find_empty();

}
