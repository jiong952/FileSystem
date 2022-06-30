package com.zjh.service;

/**
 * @author 张俊鸿
 * @description: 用户管理操作
 * @since 2022-06-30 14:11
 */
public interface UserService {
    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return {@link Boolean}
     */
    Boolean login (String userName, String password);

    /**
     * 注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return {@link Boolean}
     */
    Boolean register (String userName, String password);

    /**
     * 注销
     *
     * @return {@link Boolean}
     */
    Boolean logout ();

}
