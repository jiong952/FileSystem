package com.zjh.pojo;


import java.io.Serializable;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 文件控制块
 * @since 2022-06-30 12:34
 */
public class FCB implements Serializable {
    private String fileName; //文件名
    private Character type; //文件类型 分为目录D 普通文件N
    private IndexNode indexNode; //索引结点
    private FCB father; //父节点
    private List<FCB> children; //孩子集合

    public FCB(String fileName, Character type, IndexNode indexNode, FCB father, List<FCB> children) {
        this.fileName = fileName;
        this.type = type;
        this.indexNode = indexNode;
        this.father = father;
        this.children = children;
    }

    public FCB() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public IndexNode getIndexNode() {
        return indexNode;
    }

    public void setIndexNode(IndexNode indexNode) {
        this.indexNode = indexNode;
    }

    public FCB getFather() {
        return father;
    }

    public void setFather(FCB father) {
        this.father = father;
    }


    public List<FCB> getChildren() {
        return children;
    }

    public void setChildren(List<FCB> children) {
        this.children = children;
    }
}
