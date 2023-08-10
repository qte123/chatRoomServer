package com.tafleo.server.file;

import com.tafleo.dao.BaseDao;
import com.tafleo.dao.message.MessageDao;
import com.tafleo.dao.message.MessageDaoImpl;
import com.tafleo.pojo.Message;
import com.tafleo.server.ServerImpl;
//import com.tafleo.client.file.big.SendFile;
import com.tafleo.util.JSONUtil;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;

public class FileServerRunnable extends ServerImpl implements Runnable {
    private Socket socket;//插座
    private JTextPane textPane;
    private String fromIP;//发送方电脑IP
    private String toIP;//接收方电脑IP
    private String fileName;//文件名

    private boolean flag;

    private Message message;

    private MessageDao messageDao;
    private Connection connection;

    public FileServerRunnable(Socket socket, JTextPane textPane) {
        this.socket = socket;
        this.textPane = textPane;
    }

    @Override
    public void acceptFile() {
        //3.读取客户端的消息
        flag = false;
        try {
            messageDao = new MessageDaoImpl();
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            //输入流
            InputStream is = socket.getInputStream();
            //输入流
            //4.输出消息
            byte[] buffer = new byte[MAX];
            int len;
            // 获取本地文件夹存放地址
            //接收信息字符串
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                //消息字符串
                baos.write(buffer, 0, len);
                //String IM = new String(buffer, 0, len);
                String IM = baos.toString();
                fromIP = IM.substring(0, IM.indexOf("#"));
                toIP = IM.substring(IM.indexOf("#") + 1, IM.indexOf("$"));
                String jsonMessage = IM.substring(IM.indexOf("$") + 1);
                message = JSONUtil.JSONToEntity(jsonMessage, Message.class);
                fileName = message.getMessage();
                time();
                System.out.println("fromIP：" + fromIP + " 向 " + "toIP：" + toIP + " 发送文件");
                System.out.println(fileName);
            }
            messageDao.addMessage(connection, message);
            connection.commit();
            //输入流
           // new Thread(new SendFile(toIP, fileName)).start();
            String oldMessage = textPane.getText();
            if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
            String newMessage = oldMessage + "fromIP：" + fromIP + " 向 " + "toIP：" + toIP + " 发送文件\n 文件名："+fileName;
            textPane.setText(newMessage);
            is.close();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            flag = false;
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
    }

    @Override
    public void run() {
        acceptFile();
    }
}