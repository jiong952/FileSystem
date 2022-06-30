package com.zjh.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 张俊鸿
 * @description: 索引结点
 * @since 2022-06-30 12:30
 */
public class IndexNode implements Serializable {
    private String permission; //文件访问权限 6位 前三位创建者 后三位其他用户 rwx---
    private int size; //文件大小
    private int first_block; //文件首地址（第一个盘块号）
    private int fcbNum; //文件项个数 如果是普通文件为0 目录看其下有多少个
    private String creator; //创建者
    private Date updateTime; //文件修改时间

    public IndexNode(String permission, int size, int first_block, int fcbNum, String creator, Date updateTime) {
        this.permission = permission;
        this.size = size;
        this.first_block = first_block;
        this.fcbNum = fcbNum;
        this.creator = creator;
        this.updateTime = updateTime;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public IndexNode() {
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getFcbNum() {
        return fcbNum;
    }

    public void addFcbNum() {
        this.fcbNum++;
    }
    public void subFcbNum() {
        this.fcbNum--;
    }
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFirst_block() {
        return first_block;
    }

    public void setFirst_block(int first_block) {
        this.first_block = first_block;
    }
}
