package com.zjh;

import com.zjh.constant.Constants;
import com.zjh.pojo.Disk;
import com.zjh.service.*;
import com.zjh.service.impl.*;
import com.zjh.utils.Utility;

import java.util.Scanner;

/**
 * @author 张俊鸿
 * @description: 启动类
 * @since 2022-06-30 0:20
 */
public class Application {
    private static final DataService dataService = new DataServiceImpl();
    private static final UserService userService = new UserServiceImpl();
    private static final FileService fileService = new FileServiceImpl();
    private static final DirService dirService = new DirServiceImpl();
    private static final DiskService diskService = new DiskServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //加载磁盘数据
        Boolean flag = dataService.loadData(Constants.SAVE_PATH);
        if(!flag){
            //加载失败请求初始化新的
            System.out.println("是否重新初始化一个磁盘空间（Y/N）");
            if(scanner.nextLine().equalsIgnoreCase("Y")){
                dataService.init();
            }else {
                System.exit(0);
            }
        }
        Boolean login = false;
        while (true){
            System.out.println("登录 / 注册 / 退出");
            String nextLine = scanner.nextLine();
            String[] inputs = Utility.inputResolve(nextLine);
            switch (inputs[0]){
                case "login":
                    System.out.print("用户名:");
                    String username = scanner.nextLine();
                    System.out.print("密码:");
                    String pwd = scanner.nextLine();
                    login = userService.login(username, pwd);
                    if(login){
                        System.out.println("进入系统.....");
                        Boolean logout = false;
                        while (!logout){
                            dirService.showPath();
                            String nextLine2 = scanner.nextLine();
                            String[] inputs2 = Utility.inputResolve(nextLine2);
                            switch (inputs2[0]){
                                case "logout":
                                    logout = userService.logout();
                                    break;
                                case "pwd":
                                    dirService.pwd();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    break;
                case "register":
                    System.out.print("用户名:");
                    String username2 = scanner.nextLine();
                    System.out.print("密码:");
                    String pwd2 = scanner.nextLine();
                    userService.register(username2,pwd2);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }
}
