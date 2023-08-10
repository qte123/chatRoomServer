package com.tafleo.view.menu;

import com.tafleo.client.action.ActionClient;
import com.tafleo.client.sidebar.SidebarClient;
import com.tafleo.dao.BaseDao;
import com.tafleo.dao.user.UserDao;
import com.tafleo.dao.user.UserDaoImpl;
import com.tafleo.pojo.User;
import com.tafleo.util.JSONUtil;
import com.tafleo.view.ViewImpl;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Information extends ViewImpl {

    private JFrame frame;
    private User user;
    private JTextField textField;//用户名
    private JComboBox comboBox_1;//性别
    private JTextArea textArea;//签名
    private JTextField textField_1;//出生年月
    private JTextField textField_2;//电话号码
    private JTextField textField_3;//故乡
    private JTextField textField_4;//电子邮箱
    private JTextField textField_7;//星座
    private JLabel lblNewLabel_7;//头像
    private JLabel lblNewLabel_9;//状态
    private UserDao userDao;
    private Connection connection;
    private List<User> list;
    private JLabel lblNewLabel_11;
    private JTable table;
    private Object[][] l;
    private String[] s;
    private String time;
    private JTextPane textPane;
    private JLabel lblNewLabel_222;

    public Information(User user, List<User> list, JLabel lblNewLabel_11, JTable table, Object[][] l, String[] s, JTextPane textPane) {
        this.user = user;
        this.list = list;
        this.lblNewLabel_11 = lblNewLabel_11;
        this.table = table;
        this.l = l;
        this.s = s;
        this.textPane = textPane;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    information();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void downLine() {
        try {
            user.setChatStatus(0);
            userDao.modify(connection, user);
            connection.commit();
            String str = JSONUtil.entityToJSON(user);
            new Thread(new SidebarClient("255.255.255.255", "exit*" + str)).start();
            System.out.println(user);
            userDao.deleteUser(list, user, lblNewLabel_11, table, l, s);
            time = time();
            System.out.println("当前时间：" + time);
            System.out.println("账号：" + user.getChatNumber() + " 已退出服务器");
            String oldMessage = textPane.getText();
            if (!"".equals(oldMessage)) oldMessage = oldMessage + "\n";
            String newMessage = oldMessage + "当前时间：" + time + "\n" + "账号：" + user.getChatNumber() + " 已退出服务器";
            textPane.setText(newMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void information() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("信息界面");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WINDOW_ICON_FILE));
        frame.setBounds(100, 100, 488, 298);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        userDao = new UserDaoImpl();
        connection = BaseDao.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JLabel lblNewLabel = new JLabel("TT号：");
        lblNewLabel.setBounds(211, 63, 54, 15);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("用户名：");
        lblNewLabel_1.setBounds(211, 38, 54, 15);
        frame.getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("性别：");
        lblNewLabel_2.setBounds(211, 88, 42, 15);
        frame.getContentPane().add(lblNewLabel_2);

        JLabel label = new JLabel("电话号码：");
        label.setBounds(211, 138, 70, 15);
        frame.getContentPane().add(label);

        JLabel lblNewLabel_3 = new JLabel("状态：");
        lblNewLabel_3.setBounds(46, 148, 54, 15);
        frame.getContentPane().add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("故乡：");
        lblNewLabel_4.setBounds(211, 191, 54, 15);
        frame.getContentPane().add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("电子邮箱：");
        lblNewLabel_5.setBounds(211, 216, 72, 15);
        frame.getContentPane().add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("星座：");
        lblNewLabel_6.setBounds(211, 241, 54, 15);
        frame.getContentPane().add(lblNewLabel_6);

        JLabel label_1 = new JLabel("签名：");
        label_1.setBounds(211, 113, 54, 15);
        frame.getContentPane().add(label_1);

        lblNewLabel_7 = new JLabel("");
        lblNewLabel_7.setBounds(46, 38, 100, 100);
        lblNewLabel_7.setIcon(pic(user.getHeadPortraitURL(), 100, 100));
        frame.getContentPane().add(lblNewLabel_7);

        textField = new JTextField();
        textField.setText(user.getUsername());
        textField.setBounds(275, 35, 147, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setText(user.getPhoneNumber());
        textField_2.setBounds(275, 138, 147, 21);
        frame.getContentPane().add(textField_2);
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setText(user.getHome());
        textField_3.setBounds(275, 188, 147, 21);
        frame.getContentPane().add(textField_3);
        textField_3.setColumns(10);

        textField_4 = new JTextField();
        textField_4.setText(user.getEmail());
        textField_4.setBounds(275, 213, 147, 21);
        frame.getContentPane().add(textField_4);
        textField_4.setColumns(10);

        textField_7 = new JTextField();
        textField_7.setText(user.getConstellation());
        textField_7.setBounds(275, 238, 147, 21);
        frame.getContentPane().add(textField_7);
        textField_7.setColumns(10);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(275, 109, 147, 24);
        frame.getContentPane().add(scrollPane);

        textArea = new JTextArea();
        textArea.setText(user.getAutograph());
        scrollPane.setViewportView(textArea);

        comboBox_1 = new JComboBox();
        comboBox_1.setModel(new DefaultComboBoxModel(new String[]{"男", "女", "隐藏"}));
        comboBox_1.setSelectedIndex(user.getGender());
        comboBox_1.setBounds(275, 85, 147, 21);
        frame.getContentPane().add(comboBox_1);


        JLabel lblNewLabel_8 = new JLabel(user.getChatNumber());
        lblNewLabel_8.setBounds(275, 63, 147, 15);
        frame.getContentPane().add(lblNewLabel_8);

        lblNewLabel_9 = new JLabel("在线");
        if (user.getChatStatus() == 1) {
            lblNewLabel_9.setText("在线");
        } else {
            lblNewLabel_9.setText("离线");
        }
        lblNewLabel_9.setFont(new Font("宋体", Font.BOLD, 14));
        lblNewLabel_9.setBounds(81, 147, 54, 16);
        frame.getContentPane().add(lblNewLabel_9);
        JButton btnNewButton = new JButton("强制下线");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new SidebarClient(user.getRecentIP(), "down*online")).start();
                downLine();
            }
        });
        frame.getContentPane().add(btnNewButton);
        btnNewButton.setBounds(46, 188, 100, 24);
        JButton btnNewButton_1 = new JButton("删除账户");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new Thread(new SidebarClient(user.getRecentIP(), "down*delete")).start();
                try {
                    userDao.userDelete(connection, user);
                    connection.commit();
                    downLine();
                } catch (Exception exception) {
                    try {
                        connection.rollback();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    exception.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(46, 222, 100, 23);
        frame.getContentPane().add(btnNewButton_1);
        JLabel lblNewLabel_10 = new JLabel("出生年月：");
        lblNewLabel_10.setBounds(211, 166, 70, 15);
        frame.getContentPane().add(lblNewLabel_10);

        textField_1 = new JTextField();
        textField_1.setBounds(275, 163, 147, 21);
        textField_1.setText(new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()));
        frame.getContentPane().add(textField_1);

        JLabel lblNewLabel_111 = new JLabel("最近登录IP地址：");
        lblNewLabel_111.setBounds(127, 10, 108, 15);
        frame.getContentPane().add(lblNewLabel_111);

        lblNewLabel_222 = new JLabel(user.getRecentIP());
        lblNewLabel_222.setBounds(245, 10, 89, 15);
        frame.getContentPane().add(lblNewLabel_222);

        textField_1.setColumns(10);
        textField.setEditable(false);
        comboBox_1.setEnabled(false);
        textArea.setEditable(false);
        textField_1.setEditable(false);//出生年月
        textField_2.setEditable(false);//电话号码
        textField_3.setEditable(false);//故乡
        textField_4.setEditable(false);//电子邮箱
        textField_7.setEditable(false);//星座
        frame.setVisible(true);
    }
}

