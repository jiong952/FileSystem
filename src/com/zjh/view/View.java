package com.zjh.view;

import com.zjh.pojo.FCB;
import com.zjh.pojo.IndexNode;
import com.zjh.utils.Utility;

/**
 * @author 张俊鸿
 * @description: 展示交互类
 * @since 2022-06-30 15:06
 */
public class View {
    public void help(){
        System.out.println("=====command======");
        System.out.println("<login> 登录");
        System.out.println("<register> 注册");
        System.out.println("<logout> 退出");
        System.out.println("<mkdir> [dirName] 创建目录");
        System.out.println("<create> [fileName] 创建文件");
        System.out.println("<cd> [fileName] 切换到指定目录");
        System.out.println("<open> [fileName] 打开文件");
        System.out.println("<close> [fileName] 关闭文件");
        System.out.println("<read> [fileName] 读文件");
        System.out.println("<write> [fileName] 写文件");
        System.out.println("<delete> [fileName] 删除文件");
        System.out.println("<show_open> 显示打开的文件");
        System.out.println("<bitmap> 显示位示图");
        System.out.println("<ls> 显示目录文件名");
        System.out.println("=====command======");
    }
    public void showFcb(FCB fcb,int color){
        IndexNode indexNode = fcb.getIndexNode();
        System.out.printf("%-1s%-6s\t  %-2d\t  %-5s\t  %-3d\t%28s\t%-8s",
                fcb.getType(),
                indexNode.getPermission(),
                indexNode.getFcbNum(),
                indexNode.getCreator(),
                indexNode.getSize(),
                indexNode.getUpdateTime(),
                color == -1? fcb.getFileName() : Utility.getFormatLogString(fcb.getFileName(),color,0));
        System.out.println();
    }
}
