package com.zjh.service;

import com.zjh.pojo.FCB;

/**
 * @author 张俊鸿
 * @description: 目录操作
 * @since 2022-06-30 14:52
 */
public interface DirService {
    /**
     * 显示当前目录的文件项 文件个数 文件夹大小
     * 目录/权限（自己/其他） 文件项 创建者 文件大小 修改时间 文件名
     * eg. drwx———   4  root  32 2022.10.1 a.txt
     */
    void dir();

    /**
     * 创建目录
     *
     * @param dirName 文件夹名字
     * @return {@link Boolean}
     */
    Boolean mkdir(String dirName);

    /**
     * 切换目录
     * cd .. 可切换上一级
     *
     * @param dirName 目录名
     * @return {@link Boolean}
     */
    Boolean cd(String dirName);

    /**
     * 路径解析 查看是否存在该文件或目录
     *
     * @param path 路径
     * @return {@link FCB}
     */
    FCB pathResolve(String path);

    /**
     * 释放目录中所有文件占用内存（后序遍历）
     *
     * @param dirName 目录名
     * @return {@link Boolean}
     */
    Boolean freeDir(String dirName);

    /**
     * 简略打印本目录的文件名信息
     */
    void ls();

    /**
     * 树形打印所有目录及文件
     */
    void dir_tree();

    /**
     * 显示当前全目录路径
     * /zjh/a
     */
    void pwd();

    /**
     * 显示当前目录
     * /a
     */
    void showPath();
}
