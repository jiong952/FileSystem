package com.zjh.service.impl;

import com.zjh.constant.Constants;
import com.zjh.pojo.*;
import com.zjh.service.DirService;
import com.zjh.service.DiskService;
import com.zjh.service.FileService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author 张俊鸿
 * @description: 文件操作实现类
 * @since 2022-06-30 15:14
 */
public class FileServiceImpl implements FileService {
    private static final DirService dirService = new DirServiceImpl();
    private static final FileService fileService = new FileServiceImpl();
    private static final DiskService diskService = new DiskServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);
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
            //判断权限
            int permission = fileService.checkPermission(fcb);
            if(permission == 0){
                System.out.println("[error]: 无权限");
                return false;
            }
            //判断是否已经打开
            //判断是否在openFileList中
            String fill_path = dirService.pwd(fcb);
            List<OpenFile> openFileList = Memory.getInstance().getOpenFileList();
            OpenFile toWriteFile = null;
            for (OpenFile openFile : openFileList) {
                if(openFile.getFilePath().equals(fill_path)){
                    toWriteFile = openFile;
                }
            }
            if(Objects.nonNull(toWriteFile)){
                System.out.println("[error]: 文件已打开！");
                return false;
            }
            //加入openFileList中
            OpenFile openFile = new OpenFile(fcb, fill_path);
            Memory.getInstance().getOpenFileList().add(openFile);
            System.out.println("[success]: 打开成功！");
            return true;
        }
    }

    @Override
    /**显示打开文件**/
    public void show_open() {
        if(Memory.getInstance().getOpenFileList().size() == 0){
            System.out.println("<无打开文件>");
        }
        for (int i = 0; i < Memory.getInstance().getOpenFileList().size(); i++) {
            System.out.print(Memory.getInstance().getOpenFileList().get(i).getFcb().getFileName() + "\t");
        }
    }


    @Override
    public Boolean read(String filePath) {
        //判断是否存在
        FCB fcb = dirService.pathResolve(filePath);
        if(Objects.isNull(fcb)){
            System.out.println("[error]: 目标文件不存在");
            return false;
        }else if(fcb.getType().equals('D')){
            //type D 不是普通文件
            System.out.println("[error]: 无法写目录文件");
            return false;
        }else {
            //type N 普通文件
            //判断文件权限
            int permission = fileService.checkPermission(fcb);
            if(permission == 0){
                System.out.println("[error]: 无权限");
                return false;
            }
            //判断是否在openFileList中
            String fill_path = dirService.pwd(fcb);
            List<OpenFile> openFileList = Memory.getInstance().getOpenFileList();
            OpenFile toWriteFile = null;
            for (OpenFile openFile : openFileList) {
                if(openFile.getFilePath().equals(fill_path)){
                    toWriteFile = openFile;
                }
            }
            if(Objects.nonNull(toWriteFile)){
                FAT[] fats = Memory.getInstance().getFat();
                Block[] disk = Disk.getINSTANCE().getDisk();
                //从磁盘读取
                System.out.println("--------BEGIN--------");
                if(fcb.getIndexNode().getSize() == 0){
                    System.out.println("<!!!EMPTY FILE!!!>");
                    System.out.println("---------END---------");
                    return false;
                }
                FAT temp = fats[fcb.getIndexNode().getFirst_block()];
                while (temp.getNextId() != -1){
                    //遍历输出
                    System.out.print(disk[temp.getId()].getContent());
                    temp = fats[temp.getNextId()];
                }
                System.out.print(disk[temp.getId()].getContent());
                System.out.println();
                System.out.println("---------END---------");
            }else {
                System.out.println("[error]: 文件未打开 请先打开！");
                return false;
            }
        }
        return true;
    }

    @Override
    /**写入文件**/
    public Boolean write(String filePath) {
        //判断是否存在
        FCB fcb = dirService.pathResolve(filePath);
        if(Objects.isNull(fcb)){
            System.out.println("[error]: 目标文件不存在");
            return false;
        }else if(fcb.getType().equals('D')){
            //type D 不是普通文件
            System.out.println("[error]: 无法写目录文件");
            return false;
        }else {
            //type N 普通文件
            //判断文件权限
            int permission = fileService.checkPermission(fcb);
            if(permission == 0){
                System.out.println("[error]: 无权限");
                return false;
            }else if(permission == 4){
                System.out.println("[error]: 该文件是只读文件");
                return false;
            }else {
                //可写
                //判断是否在openFileList中
                String fill_path = dirService.pwd(fcb);
                List<OpenFile> openFileList = Memory.getInstance().getOpenFileList();
                OpenFile toWriteFile = null;
                for (OpenFile openFile : openFileList) {
                    if(openFile.getFilePath().equals(fill_path)){
                        toWriteFile = openFile;
                    }
                }
                if(Objects.nonNull(toWriteFile)){
                    StringBuilder content = new StringBuilder();
                    System.out.println("请输入要写入的内容（以$$结尾）:");
                    //获取用户输入 输入$$结束
                    while (true){
                        String nextLine = scanner.nextLine();
                        if(nextLine.endsWith("$$")){
                            content.append(nextLine,0,nextLine.length()-2);
                            break;
                        }else {
                            content.append(nextLine);
                            content.append("\n");
                        }
                    }
                    String choice = null;
                    if(fcb.getIndexNode().getSize() == 0){
                        //空文件 默认覆盖
                        choice = "1";
                    }else {
                        //有内容 让用户选择写入模式
                        while (true){
                            System.out.println("原文件有内容 请选择覆盖写（1）/ 追加写（2）:");
                            choice = scanner.nextLine();
                            if(choice.equals("1") || choice.equals("2")){
                                break;
                            }
                        }
                    }
                    FAT[] fats = Memory.getInstance().getFat();
                    int size = content.toString().toCharArray().length;
                    if(choice.equals("1")){
                        //覆盖写入
                        //1.如果不是空文件 则清空之前占据的盘块
                        if(fcb.getIndexNode().getSize() != 0){
                            diskService.freeFile(fcb);
                            fcb.getFather().getIndexNode().subFcbNum();
                        }
                        //2.重新写入
                        int first = diskService.writeToDisk(content.toString());
                        //3.将文件指向第一块
                        fcb.getIndexNode().setFirst_block(first);
                        //4.修改索引结点大小
                        fcb.getIndexNode().setSize(size);
                        //修改父目录项 以及一直递归修改父目录的大小
                        dirService.updateSize(fcb,true,-1);
                    }else {
                        //追加写入
                        //1.从第一块往下找  直到-1的块的块号
                        FAT temp = fats[fcb.getIndexNode().getFirst_block()];
                        while (temp.getNextId() != -1){
                            temp = fats[temp.getNextId()];
                        }
                        //2.写入要追加的内容
                        content.insert(0,'\n');
                        int append_begin = diskService.writeToDisk(content.toString());
                        //3.修改最后一块指向新的内容
                        temp.setNextId(append_begin);
                        //4.修改索引结点大小 加上原来的
                        int size_origin = fcb.getIndexNode().getSize();
                        fcb.getIndexNode().setSize(size + size_origin);
                        //修改父目录项 以及一直递归修改父目录的大小
                        dirService.updateSize(fcb,true,size);
                    }
                    System.out.println("[success]: 写入成功！");
                    return true;
                }else {
                    System.out.println("[error]: 文件未打开 请先打开！");
                    return false;
                }
            }
        }
    }

    @Override
    /**关闭文件**/
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
    /**删除文件及目录**/
    public Boolean delete(String filePath) {
        //判断是否存在
        FCB fcb = dirService.pathResolve(filePath);
        if(Objects.isNull(fcb)){
            System.out.println("[error]: 目标文件不存在");
            return false;
        }
        //判断权限 需要对文件夹具有rwx权限 对文件具有rw权限
        int per_father = fileService.checkPermission(fcb.getFather());
        int permission = fileService.checkPermission(fcb);
        if(!(per_father == 7 && (permission == 7 || permission == 6))){
            System.out.println("[error]: 无权限");
            return false;
        }
        //判断是否打开 打开要先关闭
        //判断是否在openFileList中
        String fill_path = dirService.pwd(fcb);
        List<OpenFile> openFileList = Memory.getInstance().getOpenFileList();
        OpenFile toWriteFile = null;
        for (OpenFile openFile : openFileList) {
            if(openFile.getFilePath().equals(fill_path)){
                toWriteFile = openFile;
            }
        }
        if(Objects.nonNull(toWriteFile)){
            System.out.println("[error]: 文件被打开 请先关闭");
            return false;
        }
        //重复确认
        String choice = null;
        while (true){
            System.out.println("确认删除该文件？（Y/N）");
            choice = scanner.nextLine();
            if(choice.equals("Y")) break;
            if(choice.equals("N")) {
                System.out.println("[success]: 已取消删除！");
                return false;
            }
        }
        //空文件判断
        if(fcb.getIndexNode().getSize() != 0 || fcb.getIndexNode().getFcbNum() != 0){
            if(fcb.getType().equals('D')){
                //type D 目录
                //todo 借助栈删除目录
//                diskService.freeDir(fcb);
                System.out.println("[error]: 文件夹非空 无法删除");
                return false;
            }else {
                //清空磁盘
                diskService.freeFile(fcb);
            }
        }
        //从FCB集合中去除 修改父目录文件项 修改父目录儿子结点
        Disk.getINSTANCE().getFcbList().remove(fcb);
        fcb.getFather().getIndexNode().subFcbNum();
        fcb.getFather().getChildren().remove(fcb);
        //递归修改父目录文件大小
        dirService.updateSize(fcb,false,-1);
        System.out.println("[success]: 删除成功");
        return true;
    }
    @Override
    /**
     * 查找当前用户对该文件的权限  0表示无权限 r=4,w=2,x=1 rwx=7 rw-=6 r--=4
     */
    public int checkPermission(FCB fcb) {
        int permission = 0;
        //查看是否是创建者
        String per = null;
        if(Memory.getInstance().getCurUser().getUserName().equals(fcb.getIndexNode().getCreator())){
            //前三位
            per = fcb.getIndexNode().getPermission().substring(0,3);
        }else {
            //后三位
            per = fcb.getIndexNode().getPermission().substring(3);
        }
        char[] chars = per.toCharArray();
        for (char c : chars) {
            if(c == 'r'){
                permission += Constants.READ;
            }else if(c == 'w'){
                permission += Constants.WRITE;
            }else if(c == 'x'){
                permission += Constants.EXECUTION;
            }
        }
        return permission;
    }

    @Override
    /**文件重命名**/
    public Boolean rename(String filePath, String newName) {
        //判断是否存在
        FCB fcb = dirService.pathResolve(filePath);
        if(Objects.isNull(fcb)){
            System.out.println("[error]: 目标文件不存在");
            return false;
        }
        //判断文件权限
        int permission = fileService.checkPermission(fcb);
        if(permission == 0 || permission == 4){
            System.out.println("[error]: 无权限");
            return false;
        }
        //如果是普通文件 判断是否打开
        if(fcb.getType().equals('N')){
            String fill_path = dirService.pwd(fcb);
            List<OpenFile> openFileList = Memory.getInstance().getOpenFileList();
            OpenFile toWriteFile = null;
            for (OpenFile openFile : openFileList) {
                if(openFile.getFilePath().equals(fill_path)){
                    toWriteFile = openFile;
                }
            }
            if(Objects.nonNull(toWriteFile)){
                System.out.println("[error]: 文件被打开 请先关闭！");
                return false;
            }
        }
        //进行重命名
        fcb.setFileName(newName);
        System.out.println("[success]: 修改成功！");
        return true;
    }
}
