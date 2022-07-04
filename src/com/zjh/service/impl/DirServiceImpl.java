package com.zjh.service.impl;

import com.zjh.constant.Constants;
import com.zjh.pojo.*;
import com.zjh.service.DirService;
import com.zjh.service.FileService;
import com.zjh.utils.Utility;
import com.zjh.view.View;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author 张俊鸿
 * @description: 目录操作实现类
 * @since 2022-06-30 15:13
 */
public class DirServiceImpl implements DirService {
    private static final DirService dirService = new DirServiceImpl();
    private static final FileService fileService = new FileServiceImpl();
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.insert(0,'a');
        sb.insert(0,'b');
        sb.insert(0,'c');
        System.out.println(sb.toString());
    }
    /**显示当前目录下的所有文件项、
     * 白色 普通文件
     * 绿色 可执行文件
     * 红色 压缩文件
     * 蓝色 目录文件
     * **/
    @Override
    public void dir() {
        Memory memory = Memory.getInstance();
        List<FCB> children = memory.getCurDir().getChildren();
        View view = new View();
        System.out.println("目录权限\t文件项数\t创建者\t文件大小\t\t 修改时间\t\t\t\t\t\t文件名");
        for (int i = 0; i < children.size(); i++) {
            FCB fcb = children.get(i);
            if(fcb.getType().equals('N')){
                if(Utility.getSuffix(fcb.getFileName()).equals("exe")){
                    //绿色
                    view.showFcb(fcb,36);
                }else if(Utility.getSuffix(fcb.getFileName()).equals("tar") || Utility.getSuffix(fcb.getFileName()).equals("zip") || Utility.getSuffix(fcb.getFileName()).equals("zip") || Utility.getSuffix(fcb.getFileName()).equals("rar")){
                    view.showFcb(fcb,31);
                }else {
                    //普通文件
                    view.showFcb(fcb,-1);
                }
            }else {
                //蓝色
                view.showFcb(fcb,34);
            }

        }
    }

    /**创建目录**/
    @Override
    public Boolean mkdir(String dirName,String permission) {
        FCB curDir = Memory.getInstance().getCurDir();
        User user = Memory.getInstance().getCurUser();
        List<FCB> children = curDir.getChildren();
        //判空
        if(Objects.isNull(dirName)){
            System.out.println("[error]: 目录名不可为空");
            return false;
        }
        //判断重复
        dirName = dirName.trim(); //去除首尾空格
        for (FCB child : children) {
            if(child.getFileName().equals(dirName)){
                System.out.println("[error]: 目录名重复 请重新命名");
                return false;
            }
        }
        //创建索引节点 创建FCB 文件大小为0 空文件
        IndexNode indexNode = new IndexNode(permission, 0, -1, 0, user.getUserName(), new Date());
        FCB fcb = new FCB(dirName, 'D', indexNode, curDir, new LinkedList<>());
        //将文件控制块放入磁盘的fcb集合
        Disk.getINSTANCE().getFcbList().add(fcb);
        //修改父目录的文件项 加入父目录儿子集合
        curDir.getIndexNode().addFcbNum();
        curDir.getChildren().add(fcb);
        System.out.println("[success]: 创建目录成功！");
        return true;
    }

    /**切换目录**/
    @Override
    public Boolean cd(String path) {
        //判断是否直接切换到根目录
        if("/".equals(path.trim())){
            Memory.getInstance().setCurDir(Memory.getInstance().getRootDir());
            return true;
        }
        //判断是不是..  ../
        if("..".equals(path.trim()) || "../".equals(path.trim())){
            FCB curDir = Memory.getInstance().getCurDir();
            //判断是不是已经在根目录
            if(curDir != Memory.getInstance().getRootDir()){
                //改变当前目录为父目录
                Memory.getInstance().setCurDir(curDir.getFather());
            }
            return true;

        }
        //解析路径
        FCB fcb = dirService.pathResolve(path);
        //null 不存在
        if(Objects.isNull(fcb)){
            System.out.println("[error]: 目标目录不存在");
            return false;
        }else if(fcb.getType().equals('N')){
            //type N 不是目录文件
            System.out.println("[error]: 无法进入普通文件");
            return false;
        }else {
            //type D 切换到对应目录
            //判断权限
            int permission = fileService.checkPermission(fcb);
            if(permission == 0){
                System.out.println("[error]: 无权限");
                return false;
            }
            Memory.getInstance().setCurDir(fcb);
        }
        return null;
    }

    /**解析路径**/
    @Override
    public FCB pathResolve(String path) {
        path = path.trim();
        FCB curDir = Memory.getInstance().getCurDir();
        FCB rootDir = Memory.getInstance().getRootDir();
        //判断是不是/开头 不是就是当前目录找
        if(!path.startsWith("/")){
            for (FCB child : curDir.getChildren()) {
                if(child.getFileName().equals(path)){
                    return child;
                }
            }
        }else {
            //以/开头 从根目录逐层往下找
            path = path.substring(1);
            String[] splitDir = path.split("/");
            FCB temp = rootDir;
            for (int i = 0; i < splitDir.length - 1; i++) {
                //找到目标文件所在目录
                for (FCB child : temp.getChildren()) {
                    if(child.getFileName().equals(splitDir[i])){
                        temp = child;
                        continue;
                    }
                }
            }
            //在该目录下找
            for (FCB child : temp.getChildren()) {
                if(child.getFileName().equals(splitDir[splitDir.length - 1])){
                    return child;
                }
            }
        }
        return null;
    }

    /**更新目录大小**/
    @Override
    public void updateSize(FCB fcb, Boolean isAdd, int new_add) {
        FCB temp = fcb.getFather();
        while (temp != Memory.getInstance().getRootDir()){
            //递归修改父目录的大小
            int size = temp.getIndexNode().getSize();
            if(isAdd){
                if(new_add == -1){
                    //增加目录大小
                    temp.getIndexNode().setSize(size + fcb.getIndexNode().getSize());
                }else {
                    temp.getIndexNode().setSize(size + new_add);
                }
            }else {
                temp.getIndexNode().setSize(size - fcb.getIndexNode().getSize());
            }
            temp = temp.getFather();
        }
    }

    /**显示当前目录下的所有文件文件名**/
    @Override
    public void ls() {
        Memory memory = Memory.getInstance();
        List<FCB> children = memory.getCurDir().getChildren();
        for (int i = 0; i < children.size(); i++) {
            FCB fcb = children.get(i);
            if(fcb.getType().equals('N')){
                if(Utility.getSuffix(fcb.getFileName()).equals("exe")){
                    System.out.print(Utility.getFormatLogString(fcb.getFileName(),36,0) + " ");
                }else if(Utility.getSuffix(fcb.getFileName()).equals("tar") || Utility.getSuffix(fcb.getFileName()).equals("zip") || Utility.getSuffix(fcb.getFileName()).equals("zip") || Utility.getSuffix(fcb.getFileName()).equals("rar")){
                    System.out.print(Utility.getFormatLogString(fcb.getFileName(),31,0) + " ");
                }else {
                    System.out.print(fcb.getFileName() + " ");
                }
            }else {
                System.out.print(Utility.getFormatLogString(fcb.getFileName(),34,0) + " ");
            }
        }
    }

    /**显示全路径**/
    @Override
    public String pwd(FCB fcb) {
        Memory memory = Memory.getInstance();
        StringBuilder sb = new StringBuilder();
        FCB temp = fcb;
        while (temp != memory.getRootDir()){
            //还没打印到根目录
            sb.insert(0,temp.getFileName());
            sb.insert(0,'/');
            temp = temp.getFather();
        }
        return sb.toString();
    }

    /**输入命令时显示当前目录**/
    @Override
    public void showPath() {
        Memory memory = Memory.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("\n[");
        sb.append(memory.getCurUser().getUserName() + "@");
        sb.append(" ");
        sb.append(memory.getCurDir().equals(memory.getRootDir()) ? "/" : memory.getCurDir().getFileName());
        sb.append("]");
        System.out.print(sb);
    }

    /**显示位示图**/
    @Override
    public void bitmap() {
        FAT[] fats = Memory.getInstance().getFat();
        for (int i = 0; i < fats.length; i++) {
            System.out.print(fats[i].getBitmap() + " ");
            if((i+1) % Constants.WORD_SIZE == 0){
                System.out.println();
            }
        }
    }
}
