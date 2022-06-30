package com.zjh.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @author 张俊鸿
 * @description: 用户类
 * @date 2022/06/30
 * @since 2022-06-30 0:23
 */
public class User implements Serializable {
    private String userName; //用户名
    private String password; //密码
    private Date lastLoginTime; //上次登录的时间

    public User(String userName, String password, Date lastLoginTime) {
        this.userName = userName;
        this.password = password;
        this.lastLoginTime = lastLoginTime;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
