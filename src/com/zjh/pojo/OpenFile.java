package com.zjh.pojo;

import java.io.Serializable;

/**
 * @author 张俊鸿
 * @description: 内存中打开的文件
 * @since 2022-06-30 12:45
 */
public class OpenFile implements Serializable {
    private FCB fcb; //文件控制块
    private String filePath; //文件全路径
}
