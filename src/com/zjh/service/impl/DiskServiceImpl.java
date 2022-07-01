package com.zjh.service.impl;

import com.zjh.constant.Constants;
import com.zjh.pojo.*;
import com.zjh.service.DirService;
import com.zjh.service.DiskService;
import com.zjh.utils.Utility;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * @author 张俊鸿
 * @description: 磁盘操作实现类
 * @since 2022-06-30 15:14
 */
public class DiskServiceImpl implements DiskService {
    private static final DirService dirService = new DirServiceImpl();
    @Override
    /**释放目录中所有文件占用内存（借助栈）**/
    public Boolean freeDir(FCB fcb) {
        Stack<FCB> fcbStack = new Stack<>();
        Queue<FCB> que = new LinkedList<>();
        List<FCB> children = fcb.getChildren();
        //层次遍历入栈
        Boolean flag = false;
        while (!flag){
            //入栈
            fcbStack.push(fcb);
            //入队列
            que.offer(fcb);
        }
        //依次出栈删除
        return null;
    }
    @Override
    /**清除文件占据的磁盘空间及改变FAT表**/
    public Boolean freeFile(FCB fcb) {
        FAT[] fats = Memory.getInstance().getFat();
        FAT temp_1 = fats[fcb.getIndexNode().getFirst_block()];
        FAT temp_2 = null;
        //1.修改FAT表 双指针改法
        while(temp_1.getNextId() != -1){
            temp_2 = temp_1;
            temp_1 = fats[temp_1.getNextId()];
            //断开前后连接
            temp_2.setNextId(-1);
            //将占据的盘块对应内容置空 （这里是假置空 考虑到直接将磁盘块置空消耗内存不实际）
            temp_2.setBitmap(0);
        }
        temp_1.setBitmap(0);
        //2.修改父目录文件项数
        fcb.getFather().getIndexNode().subFcbNum();
        //3.递归修改父目录文件大小
        dirService.updateSize(fcb,false);
        //4.索引结点大小变为0 空文件
        fcb.getIndexNode().setSize(0);
        return true;
    }

    @Override
    /**将内容写入磁盘块**/
    public int writeToDisk(String content) {
        //判断是否有足够的磁盘空间
        int needNum = Utility.ceilDivide(content.length(), Constants.BLOCK_SIZE);
        if(needNum > Memory.getInstance().getEmpty_blockNum()){
            System.out.println("[error]: 磁盘空间不足！");
            return -1;
        }
        //开始写入 双指针写入法
        FAT[] fats = Memory.getInstance().getFat();
        int first = -1;
        //找到第一个
        first = find_empty();
        int temp_1 = first;
        int temp_2 = -1;
        Block[] disk = Disk.getINSTANCE().getDisk();
        int i = 0;
        for (; i < needNum - 1; i++) {
            String splitString = content.substring(i*Constants.BLOCK_SIZE,(i+1)*Constants.BLOCK_SIZE);
            //存储到磁盘
            disk[temp_1].setContent(splitString);
            fats[temp_1].setBitmap(1);
            temp_2 = temp_1;
            //寻找下一个空闲块
            temp_1 = find_empty();
            fats[temp_2].setNextId(temp_1);
        }
        //设置最后一个块
        disk[temp_1].setContent(content.substring((i)*Constants.BLOCK_SIZE));
        fats[temp_1].setNextId(-1);
        fats[temp_1].setBitmap(1);
        //返回第一个磁盘块号
        return first;
    }

    @Override
    /**寻找空闲块**/
    public int find_empty() {
        FAT[] fats = Memory.getInstance().getFat();
        for (int i = 0; i < fats.length; i++) {
            if(fats[i].getBitmap() == 0){
                return i;
            }
        }
        return -1;
    }
}
