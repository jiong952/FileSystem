package com.zjh.service;

/**
 * @author 张俊鸿
 * @description: 文件操作
 * @since 2022-06-30 14:11
 */
public interface FileService {

    /**
     * 创建文件
     *
     * @param fileName   文件名称
     * @param permission 权限
     * @return {@link Boolean}
     */
    Boolean create(String fileName, String permission);

    /**
     * 打开文件
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    Boolean open(String filePath);

    /**
     * 显示打开的文件
     */
    void show_open();

    /**
     * 读取文件
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    Boolean read(String filePath);

    /**
     * 写入文件
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    Boolean write(String filePath);

    /**
     * 关闭文件
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    Boolean close(String filePath);

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    Boolean delete(String filePath);


    /**
     * 释放文件空间 修改FAT表和位示图
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    Boolean freeFile(String filePath);
}
