package com.zjh.service.impl;

import com.zjh.pojo.*;
import com.zjh.service.FileService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 张俊鸿
 * @description: 文件操作实现类
 * @since 2022-06-30 15:14
 */
public class FileServiceImpl implements FileService {
    @Override
    public Boolean create(String fileName,String permission) {
        FCB curDir = Memory.getInstance().getCurDir();
        User user = Memory.getInstance().getCurUser();
        List<FCB> children = curDir.getChildren();
        //判空
        if(Objects.isNull(fileName)) {
            System.out.println("[error]: 文件名不可为空");
            return false;
        }
        //判断重复
        fileName = fileName.trim(); //去除首尾空格
        for (FCB child : children) {
            if(child.getFileName().equals(fileName)){
                System.out.println("[error]: 文件名重复 请重新命名");
                return false;
            }
        }
        //创建索引节点 创建FCB 文件大小为0 空文件
        IndexNode indexNode = new IndexNode(permission, 0, -1, 0, user.getUserName(), new Date());
        FCB fcb = new FCB(fileName, 'N', indexNode, curDir, null);
        //将文件控制块放入磁盘的fcb集合
        Disk.getINSTANCE().getFcbList().add(fcb);
        //修改父目录的文件项 加入父目录儿子集合
        curDir.getIndexNode().addFcbNum();
        curDir.getChildren().add(fcb);
        System.out.println("[success]: 创建文件成功！");
        return true;
    }

    @Override
    public Boolean open(String filePath) {
        return null;
    }

    @Override
    public Boolean read(String filePath) {
        return null;
    }

    @Override
    public Boolean write(String filePath) {
        return null;
    }

    @Override
    public Boolean close(String filePath) {
        return null;
    }

    @Override
    public Boolean delete(String filePath) {
        return null;
    }


    @Override
    public Boolean freeFile(String filePath) {
        return null;
    }
}
