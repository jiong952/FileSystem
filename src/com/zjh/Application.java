package com.zjh;

import com.zjh.constant.Constants;
import com.zjh.pojo.Disk;
import com.zjh.pojo.Memory;
import com.zjh.service.*;
import com.zjh.service.impl.*;
import com.zjh.utils.Utility;
import com.zjh.view.View;

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
            System.out.println("请选择login / register / exit：");
            String nextLine = scanner.nextLine();
            String[] inputs = Utility.inputResolve(nextLine);
            switch (inputs[0]){
                case "login":
                    System.out.print("用户名: ");
                    String username = scanner.nextLine();
                    System.out.print("密码: ");
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
                                case "login":
                                    userService.login(null, null);
                                    break;
                                case "register":
                                    userService.register(null,null);
                                    break;
                                case "logout":
                                    logout = userService.logout();
                                    break;
                                case "mkdir":
                                    if(inputs2.length == 1){
                                        System.out.println("mkdir [dirName]");
                                        break;
                                    }
                                    System.out.println("请输入6位目录权限：r:读 w:写 x:执行[前三位表示自己] [后三位表示其他用户]");
                                    String permission2 = scanner.nextLine();
                                    while(permission2.length() != 6){
                                        System.out.println("请输入6位目录权限：r:读 w:写 x:执行[前三位表示自己] [后三位表示其他用户]");
                                        permission2 = scanner.nextLine();
                                    }
                                    dirService.mkdir(inputs2[1],permission2);
                                    break;
                                case "dir":
                                    dirService.dir();
                                    break;
                                case "ll":
                                    dirService.dir();
                                    break;
                                case "pwd":
                                    String path = dirService.pwd(Memory.getInstance().getCurDir());
                                    System.out.print(path);
                                    break;
                                case "create":
                                    if(inputs2.length == 1){
                                        System.out.println("create [fileName]");
                                        break;
                                    }
                                    System.out.println("请输入文件权限：r:读 w:写 x:执行[前三位表示自己] [后三位表示其他用户]");
                                    String permission = scanner.nextLine();
                                    while (permission.length() != 6){
                                        System.out.println("请输入文件权限：r:读 w:写 x:执行[前三位表示自己] [后三位表示其他用户]");
                                        permission = scanner.nextLine();
                                    }
                                    fileService.create(inputs2[1],permission);
                                    break;
                                case "cd":
                                    if(inputs2.length == 1){
                                        System.out.println("cd [fileName]");
                                        break;
                                    }
                                    dirService.cd(inputs2[1]);
                                    break;
                                case "open":
                                    if(inputs2.length == 1){
                                        System.out.println("open [fileName]");
                                        break;
                                    }
                                    fileService.open(inputs2[1]);
                                    break;
                                case "show_open":
                                    fileService.show_open();
                                    break;
                                case "close":
                                    if(inputs2.length == 1){
                                        System.out.println("close [fileName]");
                                        break;
                                    }
                                    fileService.close(inputs2[1]);
                                    break;
                                case "read":
                                    if(inputs2.length == 1){
                                        System.out.println("read [fileName]");
                                        break;
                                    }
                                    fileService.read(inputs2[1]);
                                    break;
                                case "write":
                                    if(inputs2.length == 1){
                                        System.out.println("write [fileName]");
                                        break;
                                    }
                                    fileService.write(inputs2[1]);
                                    break;
                                case "bitmap":
                                    dirService.bitmap();
                                    break;
                                case "delete":
                                    if(inputs2.length == 1){
                                        System.out.println("delete [fileName]");
                                        break;
                                    }
                                    fileService.delete(inputs2[1]);
                                    break;
                                case "rename":
                                    if(inputs2.length < 3){
                                        System.out.println("<rename> [filePath] [newName]");
                                        break;
                                    }
                                    fileService.rename(inputs2[1],inputs2[2]);
                                    break;
                                case "ls":
                                    dirService.ls();
                                    break;
                                default:
                                    new View().help();
                                    break;
                            }
                        }
                    }
                    break;
                case "register":
                    System.out.print("用户名: ");
                    String username2 = scanner.nextLine();
                    System.out.print("密码: ");
                    String pwd2 = scanner.nextLine();
                    userService.register(username2,pwd2);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    new View().help();
                    break;
            }
        }
    }
}
