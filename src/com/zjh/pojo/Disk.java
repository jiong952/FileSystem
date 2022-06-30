package com.zjh.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 张俊鸿
 * @description: 整个磁盘
 * @since 2022-06-30 12:54
 */
public class Disk implements Serializable {
    private static Disk INSTANCE;
    private Block[] disk; //磁盘
    private List<FCB> fcbList; //存储在磁盘上的FCB集合
    private Map<String, User> userMap; //存储在磁盘上的用户集合
    private FAT[] fat;//文件分配表

    public static Disk getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Disk();
        }

        return INSTANCE;
    }
    public static void setINSTANCE(Disk INSTANCE) {
        Disk.INSTANCE = INSTANCE;
    }


    public Disk() {
    }



    public Block[] getDisk() {
        return disk;
    }

    public void setDisk(Block[] disk) {
        this.disk = disk;
    }

    public List<FCB> getFcbList() {
        return fcbList;
    }

    public void setFcbList(List<FCB> fcbList) {
        this.fcbList = fcbList;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public FAT[] getFat() {
        return fat;
    }

    public void setFat(FAT[] fat) {
        this.fat = fat;
    }
}
