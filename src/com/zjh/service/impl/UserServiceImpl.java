package com.zjh.service.impl;

import com.zjh.constant.Constants;
import com.zjh.pojo.FCB;
import com.zjh.pojo.IndexNode;
import com.zjh.pojo.User;
import com.zjh.pojo.Disk;
import com.zjh.pojo.Memory;
import com.zjh.service.DataService;
import com.zjh.service.UserService;

import java.util.*;

/**
 * @author 张俊鸿
 * @description: 用户操作实现类
 * @since 2022-06-30 15:15
 */
public class UserServiceImpl implements UserService {
    Memory instance = Memory.getInstance();
    @Override
    public Boolean login(String userName, String password) {
        //判断当前是否登录
        if(Objects.nonNull(instance.getCurUser())){
            System.out.println("[error]:请先退出登录");
            return false;
        }
        //用户名或密码错误
        User user = instance.getUserMap().get(userName);
        if(Objects.isNull(user) || Objects.nonNull(user) && !user.getPassword().equals(password)){
            System.out.println("[error]:用户名或密码错误");
            return false;
        }
        //设置memory量
         //找到用户目录的FCB
        List<FCB> fcbList = Disk.getINSTANCE().getFcbList();
        for (int i = 0; i < fcbList.size(); i++) {
            if(fcbList.get(i).getFileName().equals(userName)){
                //用户目录FCB
                instance.setCurUser(user);
                instance.setCurDir(fcbList.get(i));
            }
        }
        System.out.println("[success]登录成功！ 上一次登录时间："+user.getLastLoginTime());
        user.setLastLoginTime(new Date());
        return true;
    }

    @Override
    public Boolean register(String userName, String password) {
        //判断是否登录
        if (Objects.nonNull(instance.getCurUser())) {
            System.out.println("[error]:请先退出登录");
            return false;
        }
        Map<String, User> userMap = instance.getUserMap();
        //判断是否重复
        if(Objects.nonNull(userMap.get(userName))){
            System.out.println("[error]:用户名重复");
            return false;
        }
        //放进用户集合
        userMap.put(userName,new User(userName,password,null));
        //新建一个用户FCB及索引结点 其他用户无权限
        FCB rootDir = instance.getRootDir();
        IndexNode indexNode = new IndexNode("rwx---",0,-1,0,new Date());
        FCB user_fcb = new FCB(userName,'D',indexNode, rootDir,new LinkedList<>());
        //放进fcb集合
        Disk.getINSTANCE().getFcbList().add(user_fcb);
        //修改根目录
        rootDir.getChildren().add(user_fcb);
        rootDir.getIndexNode().addFcbNum();
        System.out.println("[success]：注册成功");
        return true;
    }

    @Override
    public Boolean logout() {
        //判断
        if(Objects.isNull(instance.getCurUser())){
            System.out.println("[error]:请先登录");
            return false;
        }
        instance.setCurUser(null);
        instance.setCurDir(instance.getRootDir());
        DataService dataService = new DataServiceImpl();
        Boolean flag = dataService.saveData(Constants.SAVE_PATH);
        if(flag){
            System.exit(0);
        }
        return true;
    }
}
