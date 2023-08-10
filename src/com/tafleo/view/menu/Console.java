package com.tafleo.view.menu;

import com.tafleo.client.sidebar.SidebarClient;
import com.tafleo.dao.BaseDao;
import com.tafleo.dao.user.UserDao;
import com.tafleo.dao.user.UserDaoImpl;
import com.tafleo.pojo.User;
import com.tafleo.server.action.ActionServer;

import com.tafleo.server.file.FileServer;
import com.tafleo.server.messgae.MessageServer;
import com.tafleo.server.sidebar.SidebarServer;
import com.tafleo.util.JSONUtil;
import com.tafleo.util.ReFresh;
import com.tafleo.util.SendIP;
import com.tafleo.view.ViewImpl;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Console extends ViewImpl {
    private List<User> userList;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel;
    private JTable table;
    private JTextPane textPane;
    private UserDao userDao;
    private Connection connection;
    private Object[][] l;
    private String[] s;
    private String time;

    public Console() {
        userList = new ArrayList<>();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    console();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void console() throws Exception {

        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WINDOW_ICON_FILE));
        frame.setTitle("TT聊天室服务器端");
        frame.setBounds(100, 100, 750, 478);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label_1 = new JLabel("当前在线人数：");
        label_1.setBounds(28, 65, 107, 15);
        frame.getContentPane().add(label_1);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(28, 112, 150, 260);
        frame.getContentPane().add(scrollPane_1);
        l = new Object[100][100];
        s = new String[]{"TT号", "用户名"};
        table = new JTable();
        table.setEnabled(false);
        scrollPane_1.setViewportView(table);
        table.setModel(new DefaultTableModel(l, s));

        JLabel label = new JLabel("聊天室服务器控制台");
        label.setBounds(213, 27, 360, 47);
        frame.getContentPane().add(label);
        label.setFont(new Font("宋体", Font.PLAIN, 40));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(213, 123, 504, 290);
        frame.getContentPane().add(scrollPane);

        textPane = new JTextPane();
        scrollPane.setViewportView(textPane);


        lblNewLabel = new JLabel("0");
        lblNewLabel.setBounds(125, 65, 53, 15);
        frame.getContentPane().add(lblNewLabel);
        JButton btnNewButton = new JButton("查看用户列表");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new AllUser(userList, lblNewLabel, table, l, s, textPane);
            }
        });
        btnNewButton.setBounds(28, 381, 150, 25);
        frame.getContentPane().add(btnNewButton);
        new UserDaoImpl().userOnline(userList, lblNewLabel, table, 1);
        new Thread(new SendIP()).start();
        new Thread(new MessageServer(textPane, userList)).start();
        new Thread(new FileServer(textPane)).start();
        new Thread(new ActionServer(textPane, userList, lblNewLabel, table)).start();
        new Thread(new SidebarServer(textPane, userList, lblNewLabel, table)).start();
        JLabel lblNewLabel_1 = new JLabel("服务器系统时间：");
        lblNewLabel_1.setBounds(213, 98, 127, 15);
        frame.getContentPane().add(lblNewLabel_1);

        lblNewLabel_2 = new JLabel("2021年5月21日 00:00:00");
        new Thread(new ReFresh(lblNewLabel_2)).start();
        lblNewLabel_2.setBounds(387, 98, 150, 15);
        frame.getContentPane().add(lblNewLabel_2);
        JButton btnNewButton_1 = new JButton("退出服务器系统");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new Thread(new SidebarClient("255.255.255.255", "down*online")).start();
                downLine();
                System.exit(0);
            }
        });
        btnNewButton_1.setBounds(590, 94, 127, 23);
        frame.getContentPane().add(btnNewButton_1);
        JButton btnNewButton_2 = new JButton("用户下线");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new SidebarClient("255.255.255.255", "down*online")).start();
                downLine();
            }
        });
        btnNewButton_2.setBounds(590, 61, 127, 23);
        frame.getContentPane().add(btnNewButton_2);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                new Thread(new SidebarClient("255.255.255.255", "down*online")).start();
                downLine();
                System.exit(0);
            }
        });

        JLabel lblNewLabel_3 = new JLabel("当前在线用户");
        lblNewLabel_3.setFont(new Font("宋体", Font.PLAIN, 14));
        lblNewLabel_3.setBounds(57, 90, 95, 15);
        frame.getContentPane().add(lblNewLabel_3);
        frame.setVisible(true);
    }


    public void downLine() {
        try {
            userDao = new UserDaoImpl();
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);
                user.setChatStatus(0);
                userDao.modify(connection, user);
                connection.commit();
                String str = JSONUtil.entityToJSON(user);
                new Thread(new SidebarClient("255.255.255.255", "exit*" + str)).start();
                System.out.println(user);
                userDao.deleteUser(userList, user, lblNewLabel, table, l, s);
                time = time();
                System.out.println("当前时间：" + time);
                System.out.println("账号：" + user.getChatNumber() + " 已退出服务器");
                String oldMessage = textPane.getText();
                if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
                String newMessage = oldMessage + "当前时间：" + time + "\n" + "账号：" + user.getChatNumber() + " 已退出服务器";
                textPane.setText(newMessage);
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
