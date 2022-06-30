package com.zjh.service.impl;

import com.zjh.pojo.FCB;
import com.zjh.pojo.Memory;
import com.zjh.service.DirService;

import java.util.Objects;

/**
 * @author 张俊鸿
 * @description: 目录操作实现类
 * @since 2022-06-30 15:13
 */
public class DirServiceImpl implements DirService {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.insert(0,'a');
        sb.insert(0,'b');
        sb.insert(0,'c');
        System.out.println(sb.toString());
    }
    @Override
    public void dir() {

    }

    @Override
    public Boolean mkdir(String dirName) {
        return null;
    }

    @Override
    public Boolean cd(String dirName) {
        return null;
    }

    @Override
    public FCB pathResolve(String path) {
        return null;
    }

    @Override
    public Boolean freeDir(String dirName) {
        return null;
    }

    @Override
    public void ls() {

    }

    @Override
    public void dir_tree() {

    }

    @Override
    /**显示全路径**/
    public void pwd() {
        Memory memory = Memory.getInstance();
        StringBuilder sb = new StringBuilder();
        FCB temp = memory.getCurDir();
        while (temp != memory.getRootDir()){
            //还没打印到根目录
            sb.insert(0,memory.getCurDir().getFileName());
            sb.insert(0,'/');
            temp = temp.getFather();
        }
        System.out.println(sb);
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
}
