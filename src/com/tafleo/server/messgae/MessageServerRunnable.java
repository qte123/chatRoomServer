package com.tafleo.server.messgae;

import com.tafleo.client.message.MessageClient;
import com.tafleo.dao.BaseDao;
import com.tafleo.dao.message.MessageDao;
import com.tafleo.dao.message.MessageDaoImpl;
import com.tafleo.dao.user.UserDao;
import com.tafleo.dao.user.UserDaoImpl;
import com.tafleo.pojo.Message;
import com.tafleo.pojo.User;
import com.tafleo.server.ServerImpl;

import com.tafleo.util.JSONUtil;

import javax.swing.*;
import java.net.DatagramPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MessageServerRunnable extends ServerImpl implements Runnable {
    private DatagramPacket packet;//建立数据包
    private PreparedStatement preparedStatement;//预编译
    private JTextPane textPane;
    private MessageDao messageDao;
    private List<User> list;
    private UserDao userDao;
    private Connection connection;

    public MessageServerRunnable(DatagramPacket packet, JTextPane textPane, List<User> list) {
        this.packet = packet;
        this.textPane = textPane;
        this.list = list;
    }

    @Override
    public void acceptMessage() {
        try {
            userDao = new UserDaoImpl();
            //数据库连接
            connection = BaseDao.getConnection();
            messageDao = new MessageDaoImpl();
            connection.setAutoCommit(false);
            //存放的IP和消息
            String IM = new String(packet.getData(), 0, packet.getLength());
            //发送方电脑的IP地址
            //存放的信息
            String fromIP = IM.substring(0, IM.indexOf("#"));
            //接收方电脑的IP地址
            String toIP = IM.substring(IM.indexOf("#") + 1, IM.indexOf("$"));
            String message = IM.substring(IM.indexOf("$") + 1);
            Message message1 = JSONUtil.JSONToEntity(message, Message.class);
            messageDao.addMessage(connection, message1);
            connection.commit();
            String time = time();
            String fromChatNumber = message1.getFromChatNumber();
            String toChatNumber = message1.getToChatNumber();
            String htmlText = message1.getMessage();
            User user = userDao.getUser(list, fromChatNumber);
            System.out.println(toIP);
            if ("255.255.255.255".equals(toIP)) {
                new Thread(new MessageClient(fromIP, toIP, false + "@" + htmlText + "*^%" + fromChatNumber)).start();
                System.out.println("aaaaaaaaaaaaaaaaaa");
            } else {
                new Thread(new MessageClient(fromIP, toIP, true + "@" + htmlText + "*^%" + fromChatNumber)).start();
                System.out.println("bbbbbbbbbbbbbbbbbbbb");
            }
            System.out.println("fromChatNumber：" + fromChatNumber + " ---> " + "toChatNumber：" + toChatNumber + " 正在通信...");
            System.out.println("传输的信息：" + message);
            String oldMessage = textPane.getText();
            if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
            String newMessage = oldMessage + "fromIP：" + fromIP + " ---> " + "toIP：" + toIP + " 正在通信...";
            textPane.setText(newMessage);
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
    }

    @Override
    public void run() {
        acceptMessage();
    }
}
