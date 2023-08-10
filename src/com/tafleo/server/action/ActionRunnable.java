package com.tafleo.server.action;

import com.tafleo.client.action.ActionClient;
import com.tafleo.client.file.FileClient;
import com.tafleo.client.sidebar.SidebarClient;
import com.tafleo.dao.BaseDao;
import com.tafleo.dao.user.UserDao;
import com.tafleo.dao.user.UserDaoImpl;
import com.tafleo.pojo.User;
import com.tafleo.server.ServerImpl;
import com.tafleo.util.JSONUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ActionRunnable extends ServerImpl implements Runnable {
    private Socket socket;
    private String message;
    private String fromIP;//发送方电脑IP
    private String toIP;//接收方电脑IP
    private JTextPane textPane;
    private DatagramPacket packet;
    private Connection connection;
    private String time;
    private UserDao userDao;
    private List<User> list;
    private JLabel lblNewLabel;
    private JTable table;
    private Object[][] l;
    private String[] s;

    public ActionRunnable(JTextPane textPane, DatagramPacket packet, List<User> list, JLabel lblNewLabel, JTable table) {
        this.textPane = textPane;
        this.packet = packet;
        this.list = list;
        this.lblNewLabel = lblNewLabel;
        this.table = table;
        l = new Object[100][100];
        s = new String[]{"TT号", "用户名"};
    }

    @Override
    public void run() {
        try {
            userDao = new UserDaoImpl();
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
            if (message.startsWith("*ReGiStEr%")) {
                time = time();
                System.out.println("当前时间：" + time);
                new Thread(new ActionClient(fromIP, userDao.getNumber(connection, 6))).start();
                System.out.println("fromIP：" + fromIP + " 正在注册账号");
                String oldMessage = textPane.getText();
                if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
                String newMessage = oldMessage + "当前时间：" + time + "\n" + "fromIP：" + fromIP + " 正在注册账号";
                textPane.setText(newMessage);
            }
            if (message.startsWith("save")) {
                boolean flag = false;

                User user = JSONUtil.JSONToEntity(json, User.class);
                User user1 = userDao.getUser(connection, user.getChatNumber());
                user.setFriendChatNumber("");
                int updateRows = 0;
                if (user1 == null) {
                    updateRows = userDao.addUser(connection, user);
                }
                connection.commit();
                if (updateRows > 0) {
                    flag = true;
                } else {
                    flag = false;
                }
                time = time();
                System.out.println("当前时间：" + time);
                System.out.println("服务器正在保存fromIP：" + fromIP + "注册的账号");
                String oldMessage = textPane.getText();
                if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
                String newMessage = oldMessage + "当前时间：" + time + "\n" + "服务器正在保存fromIP：" + fromIP + "注册的账号";
                textPane.setText(newMessage);
            }
            if (message.startsWith("login")) {
                User user = JSONUtil.JSONToEntity(json, User.class);
                String chatNumber = user.getChatNumber();
                String password = user.getPassword();
                User loginUser = userDao.getLoginUser(connection, chatNumber, password);
                if (loginUser != null) {
                    if (loginUser.getChatStatus() != 1) {
                        loginUser.setChatStatus(1);
                        loginUser.setRecentTime(new Date());
                        loginUser.setRecentIP(fromIP);
                        userDao.modify(connection, loginUser);
                        connection.commit();
                        List<User> userList = userDao.getUserList(connection, loginUser);
                        String userJson = JSONUtil.entityToJSON(loginUser);
                        String listJson = JSONUtil.entityListToJSON(userList);
                        if (userList == null) {
                            listJson = "[]";
                        }
                        System.out.println(listJson);
                        String filePath = "res//userList";
                        File file = new File(filePath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        OutputStream out = new FileOutputStream(file + "//" + loginUser.getChatNumber() + ".json");
                        byte[] bytes = listJson.getBytes();
                        out.write(bytes);

                        new Thread(new FileClient("127.0.0.1", loginUser.getRecentIP(), loginUser.getChatNumber() + ".json", filePath, true, null)).start();
                        new Thread(new ActionClient(fromIP, "json*" + userJson)).start();
                        new Thread(new SidebarClient("255.255.255.255", "on*" + userJson)).start();
                        userDao.userAdd(list, loginUser, lblNewLabel, table, l, s);
                        out.close();
                    } else {
                        new Thread(new ActionClient(fromIP, "json*already")).start();
                    }

                }
            }
            if (message.startsWith("successful")) {
                time = time();
                System.out.println("当前时间：" + time);
                System.out.println("账号：" + json + " 已接入服务器");
                String oldMessage = textPane.getText();
                if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
                String newMessage = oldMessage + "当前时间：" + time + "\n" + "账号：" + json + " 已接入服务器";
                textPane.setText(newMessage);
            }
            if (message.startsWith("start")) {
                User user = userDao.getUser(connection, json);
                String s = JSONUtil.entityToJSON(user);
                new Thread(new ActionClient(fromIP, "start*" + s)).start();
            }
            if (message.startsWith("exit")) {
                User user1 = JSONUtil.JSONToEntity(json, User.class);
                user1.setChatStatus(0);
                userDao.modify(connection, user1);
                connection.commit();
                String str = JSONUtil.entityToJSON(user1);
                new Thread(new SidebarClient("255.255.255.255", "exit*" + str)).start();
                System.out.println(user1);
                userDao.deleteUser(list, user1, lblNewLabel, table, l, s);
                time = time();
                System.out.println("当前时间：" + time);
                System.out.println("账号：" + user1.getChatNumber() + " 已退出服务器");
                String oldMessage = textPane.getText();
                if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
                String newMessage = oldMessage + "当前时间：" + time + "\n" + "账号：" + user1.getChatNumber() + " 已退出服务器";
                textPane.setText(newMessage);
            }
            if (message.startsWith("yesModify")) {
                String chatNumber = json.substring(0, json.indexOf("^"));
                String oldPassword = json.substring(json.indexOf("^") + 1, json.indexOf("%"));
                String newPassword = json.substring(json.indexOf("%") + 1);
                User user = userDao.getUser(connection, chatNumber);
                connection.commit();
                if (user != null) {
                    if (user.getPassword().equals(oldPassword)) {
                        user.setPassword(newPassword);
                        userDao.modify(connection, user);
                        connection.commit();
                        new Thread(new ActionClient(fromIP, "modify*Successful")).start();
                    } else {
                        new Thread(new ActionClient(fromIP, "modify*Failed")).start();
                    }
                }
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
