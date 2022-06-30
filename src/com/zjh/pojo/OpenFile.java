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

    public OpenFile(FCB fcb, String filePath) {
        this.fcb = fcb;
        this.filePath = filePath;
    }

    public FCB getFcb() {
        return fcb;
    }

    public void setFcb(FCB fcb) {
        this.fcb = fcb;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
