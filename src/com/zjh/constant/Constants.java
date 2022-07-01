package com.zjh.constant;

/**
 * @author 张俊鸿
 * @description: 常量类
 * @since 2022-06-30 12:57
 */
public interface Constants {
    String SAVE_PATH = "./system.data"; //系统文件保存位置
    int BLOCK_COUNT = 256; //磁盘块数
    int BLOCK_SIZE = 8; //块大小 单位B
    int WORD_SIZE = 16; //位示图字大小
    int READ = 4; //读权限
    int WRITE = 2; //写权限
    int EXECUTION = 1; //执行权限
}
