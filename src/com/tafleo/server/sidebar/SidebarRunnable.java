package com.tafleo.server.sidebar;

import com.tafleo.client.file.FileClient;
import com.tafleo.client.sidebar.SidebarClient;
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
import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SidebarRunnable extends ServerImpl implements Runnable {
    private Socket socket;
    private String message;
    private String fromIP;//发送方电脑IP
    private String toIP;//接收方电脑IP
    private JTextPane textPane;
    private DatagramPacket packet;
    private Connection connection;
    private String time;
    private UserDao userDao;
    private MessageDao messageDao;
    private List<User> list;
    private JLabel lblNewLabel;
    private JTable table;
    private Object[][] l;
    private String[] sl;

    public SidebarRunnable(JTextPane textPane, DatagramPacket packet, List<User> list, JLabel lblNewLabel, JTable table) {
        this.textPane = textPane;
        this.packet = packet;
        this.list = list;
        this.lblNewLabel = lblNewLabel;
        this.table = table;
        l = new Object[100][100];
        sl = new String[]{"TT号", "用户名"};
    }

    @Override
    public void run() {
        try {
            userDao = new UserDaoImpl();
            messageDao = new MessageDaoImpl();
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            //存放的IP和消息
            String IM = new String(packet.getData(), 0, packet.getLength());
            //发送方电脑的IP地址
            String fromIP = IM.substring(0, IM.indexOf("#"));
            //接收方电脑的IP地址
            String toIP = IM.substring(IM.indexOf("#") + 1, IM.indexOf("$"));
            //存放的信息
            String message = IM.substring(IM.indexOf("$") + 1);
            String json = message.substring(message.indexOf("*") + 1);
            if (message.startsWith("modify")) {
                User user = JSONUtil.JSONToEntity(json, User.class);
                System.out.println(user);
                if (user != null) {
                    userDao.modify(connection, user);
                    connection.commit();
                    userDao.modifyUserList(list, user, lblNewLabel, table, l, sl);
                    time = time();
                    System.out.println("当前时间：" + time);
                    System.out.println("账号：" + user.getChatNumber() + " 修改了用户信息");
                    String oldMessage = textPane.getText();
                    if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
                    String newMessage = oldMessage + "当前时间：" + time + "\n" + "账号：" + user.getChatNumber() + " 用户修改了信息";
                    textPane.setText(newMessage);
                    new Thread(new SidebarClient("255.255.255.255", "mod*" + json)).start();
                }
            }
            if (message.startsWith("select")) {
                User user = userDao.getUser(connection, json);
                String s = JSONUtil.entityToJSON(user);
                new Thread(new SidebarClient(fromIP, "select*" + s)).start();
            }
            if (message.startsWith("addFriend")) {
                String chatNumber = json.substring(0, json.indexOf("&"));
                String jsonUser = json.substring(json.indexOf("&") + 1);
                User user = userDao.getUser(connection, chatNumber);
                User user1 = JSONUtil.JSONToEntity(jsonUser, User.class);
                String s = JSONUtil.entityToJSON(user);
                new Thread(new SidebarClient(user1.getRecentIP(), "addFriend*" + s)).start();
            }
            if (message.startsWith("addYes")) {
                String use = json.substring(0, json.indexOf("%"));
                String use1 = json.substring(json.indexOf("%") + 1);
                User user = JSONUtil.JSONToEntity(use, User.class);
                User user1 = JSONUtil.JSONToEntity(use1, User.class);
                String s = "";
                boolean flag = false, flag1 = false;
                if (user.getFriendChatNumber() != null && !user.getFriendChatNumber().isEmpty()) {
                    String[] split = user.getFriendChatNumber().split(",");
                    if (split != null) {
                        for (int i = 0; i < split.length; i++) {
                            if (user1.getFriendChatNumber().equals(split[i])) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            s = user.getFriendChatNumber();
                        } else {
                            s = user.getFriendChatNumber() + user1.getChatNumber() + ",";
                        }
                    }
                } else {
                    s = user1.getChatNumber() + ",";
                }
                String s1 = "";
                if (user1.getFriendChatNumber() != null && !user1.getFriendChatNumber().isEmpty()) {
                    String[] split1 = user1.getFriendChatNumber().split(",");
                    if (split1 != null) {
                        for (int i = 0; i < split1.length; i++) {
                            if (user.getFriendChatNumber().equals(split1[i])) {
                                flag1 = true;
                                break;
                            }
                        }
                        if (flag1) {
                            s1 = user1.getFriendChatNumber();
                        } else {
                            s1 = user1.getFriendChatNumber() + user.getChatNumber() + ",";
                        }
                    }
                } else {
                    s1 = user.getChatNumber() + ",";
                }
                user.setFriendChatNumber(s);
                user1.setFriendChatNumber(s1);
                new Thread(new SidebarClient(user.getRecentIP(), "addYes*" + use1)).start();
                userDao.modifyUserList(list,user,lblNewLabel,table,l,sl);
                userDao.modifyUserList(list,user1,lblNewLabel,table,l,sl);
                userDao.modify(connection, user);
                connection.commit();
                userDao.modify(connection, user1);
                connection.commit();
            }
            if (message.startsWith("addNo")) {
                User user = JSONUtil.JSONToEntity(json, User.class);
                new Thread(new SidebarClient(user.getRecentIP(), "addNo*")).start();
            }
            if (message.startsWith("delete")) {
                String use = json.substring(0, json.indexOf("%"));
                String use1 = json.substring(json.indexOf("%") + 1);
                User user = JSONUtil.JSONToEntity(use, User.class);
                User user1 = JSONUtil.JSONToEntity(use1, User.class);
                new Thread(new SidebarClient(user.getRecentIP(), "delete*" + use1)).start();
                String[] userList = user.getFriendChatNumber().split(",");
                String[] user1List = user1.getFriendChatNumber().split(",");
                String s = "";
                String s1 = "";
                for (int i = 0; i < userList.length; i++) {
                    if (userList[i].equals(user1.getChatNumber())) {
                        continue;
                    }
                    s = s + userList[i] + ",";
                }
                for (int i = 0; i < user1List.length; i++) {
                    if (user1List[i].equals(user.getChatNumber())) {
                        continue;
                    }
                    s1 = s1 + user1List[i] + ",";
                }
                user.setFriendChatNumber(s);
                user1.setFriendChatNumber(s1);
                userDao.modifyUserList(list,user,lblNewLabel,table,l,sl);
                userDao.modifyUserList(list,user1,lblNewLabel,table,l,sl);
                userDao.modify(connection, user);
                connection.commit();
                userDao.modify(connection, user1);
                connection.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
    }
}
