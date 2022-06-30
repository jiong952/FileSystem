package com.zjh.service.impl;

import com.zjh.pojo.*;
import com.zjh.service.DirService;
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
    private static final DirService dirService = new DirServiceImpl();
    @Override
    /**创建文件**/
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
    /**打开文件**/
    public Boolean open(String filePath) {
        //使用pathResolve解析
        FCB fcb = dirService.pathResolve(filePath);
        //null 不存在
        if(Objects.isNull(fcb)){
            System.out.println("[error]: 目标文件不存在");
            return false;
        }else if(fcb.getType().equals('D')){
            //type D 不是普通文件
            System.out.println("[error]: 无法打开目录文件");
            return false;
        }else {
            //type N 普通文件
            //加入openFileList中
            String fill_path = dirService.pwd(fcb);
            OpenFile openFile = new OpenFile(fcb, fill_path);
            Memory.getInstance().getOpenFileList().add(openFile);
            System.out.println("[success]: 打开成功！");
            return true;
        }
    }

    @Override
    /**显示打开文件**/
    public void show_open() {
        for (int i = 0; i < Memory.getInstance().getOpenFileList().size(); i++) {
            System.out.print(Memory.getInstance().getOpenFileList().get(i).getFcb().getFileName() + "\t");
        }
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
        //判断是否存在
        FCB fcb = dirService.pathResolve(filePath);
        if(Objects.isNull(fcb)){
            System.out.println("[error]: 目标文件不存在");
            return false;
        }else if(fcb.getType().equals('D')){
            //type D 不是普通文件
            System.out.println("[error]: 无法关闭目录文件");
            return false;
        }else {
            //type N 普通文件
            //判断是否在openFileList中
            String fill_path = dirService.pwd(fcb);
            List<OpenFile> openFileList = Memory.getInstance().getOpenFileList();
            for (OpenFile openFile : openFileList) {
                if(openFile.getFilePath().equals(fill_path)){
                    //修改fcb的updateTime
                    fcb.getIndexNode().setUpdateTime(new Date());
                    //从openFileList中移除
                    openFileList.remove(openFile);
                    System.out.println("[success]: 关闭成功！");
                    return true;

                }
            }
            System.out.println("[error]: 文件未打开 无需关闭");
            return false;
        }

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
