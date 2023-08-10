package com.tafleo.server;

import com.tafleo.pojo.User;

import java.sql.Connection;

public interface Server {
    public void acceptMessage();//接收消息
    public void acceptFile();//接收文件
}
