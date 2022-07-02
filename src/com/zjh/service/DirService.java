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
     * @param dirName    目录名
     * @param permission 权限
     * @return {@link Boolean}
     */
    Boolean mkdir(String dirName,String permission);

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
     * 递归修改父目录大小
     *
     * @param fcb   FCB
     * @param isAdd 添加文件 add
     */
    void updateSize(FCB fcb,Boolean isAdd);
    /**
     * 简略打印本目录的文件名信息
     */
    void ls();

    /**
     * 显示当前全目录路径
     * /zjh/a
     *
     * @param fcb 指定目录
     * @return {@link String} 全路径
     */
    String pwd(FCB fcb);

    /**
     * 显示当前目录
     * /a
     */
    void showPath();

    /**
     * 显示位示图
     */
    void bitmap();
}
