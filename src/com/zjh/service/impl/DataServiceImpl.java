package com.zjh.service.impl;

import com.zjh.constant.Constants;
import com.zjh.pojo.*;
import com.zjh.pojo.Disk;
import com.zjh.pojo.Memory;
import com.zjh.service.DataService;

import java.io.*;
import java.util.*;

/**
 * @author 张俊鸿
 * @description: 持久化接口实现类
 * @since 2022-06-30 15:12
 */
public class DataServiceImpl implements DataService {
    @Override
    public void init() {
        Disk newDisk = Disk.getINSTANCE();
        Block[] disk = new Block[Constants.BLOCK_COUNT];
        //初始化磁盘
        for (int i = 0; i < Constants.BLOCK_COUNT; i++) {
            disk[i] = new Block();
            disk[i].setId(i);
            disk[i].setBlockSize(Constants.BLOCK_SIZE);
            disk[i].setContent(null);
        }
        newDisk.setDisk(disk);
        //初始化FCB集合
        List<FCB> fcbList = new ArrayList<>();
        newDisk.setFcbList(fcbList);
        //初始化根目录
        IndexNode indexNode = new IndexNode("rwxrwx",0,-1,0,new Date());
        FCB rootDir = new FCB("rootDir",'D',indexNode,null,new LinkedList<>());
        fcbList.add(rootDir);

        //初始化FAT表
        FAT[] fats = new FAT[Constants.BLOCK_COUNT];
        for (int i = 0; i < Constants.BLOCK_COUNT; i++) {
            fats[i] = new FAT();
            fats[i].setId(i);
            fats[i].setBitmap(0);
            fats[i].setNextId(-1);
        }
        newDisk.setFat(fats);
        //初始化内存
        Memory memory = Memory.getInstance();
        //用户集合
        Map<String, User> userMap = new HashMap<>();
        newDisk.setUserMap(userMap);
        //初始化内存
        memory.setUserMap(userMap);
        memory.setCurDir(rootDir);
        memory.setCurUser(null);
        memory.setRootDir(rootDir);
        memory.setFat(fats);
        ArrayList<OpenFile> openFiles = new ArrayList<>();
        memory.setOpenFileList(openFiles);
        memory.setEmpty_blockNum(Constants.BLOCK_COUNT);
        System.out.println("[success]初始化成功");
    }

    @Override
    public Boolean loadData(String dataPath) {
        File file = new File(dataPath);
        if (!file.exists()) {
            System.out.println("[error]:找不到文件");
            return false;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            //加载磁盘数据
            Disk.setINSTANCE((Disk) ois.readObject());
            Disk instance = Disk.getINSTANCE();
            //将磁盘数据调入内存
            Memory memory = Memory.getInstance();
            memory.setUserMap(instance.getUserMap());
            memory.setCurUser(null);
            memory.setRootDir(instance.getFcbList().get(0));
            memory.setCurDir(instance.getFcbList().get(0));
            FAT[] fats = instance.getFat();
            memory.setFat(fats);
            List<OpenFile> openFileList = new ArrayList<>();
            memory.setOpenFileList(openFileList);
            int empty_blockNum = 0;
            for (int i = 0; i < fats.length; i++) {
                if(fats[i].getBitmap() == 0){
                    empty_blockNum++;
                }
            }
            memory.setEmpty_blockNum(empty_blockNum);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[error]:IO异常");
            return false;
        }finally {
            try {
                if (Objects.nonNull(ois)) {
                    ois.close();
                }
            } catch (IOException ignored) {
            }
        }
        System.out.println("[success]加载数据成功");
        return true;
    }

    @Override
    public Boolean saveData(String savePath) {
        File file = new File(savePath);
        // 检查文件是否存在
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                if (new File(file.getParentFile().getPath()).mkdirs()) {
                    try {
                        if (!file.createNewFile()) {
                            System.out.println("[error]:保存失败");
                            return false;
                        }
                    } catch (IOException ioException) {
                        System.out.println("[error]:IO异常");
                        return false;
                    }
                } else {
                    System.out.println("[error]:保存失败");
                    return false;
                }
            }
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            // 持久化到保存文件中
            oos.writeObject(Disk.getINSTANCE());
            oos.flush();
            System.out.println("[success]保存数据成功");
            return true;
        } catch (IOException e) {
            System.out.println("[error]:保存失败");
            return false;
        } finally {
            try {
                if (Objects.nonNull(oos)) {
                    oos.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
