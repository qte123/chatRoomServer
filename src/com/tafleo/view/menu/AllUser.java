package com.tafleo.view.menu;

import com.tafleo.dao.BaseDao;
import com.tafleo.dao.user.UserDao;
import com.tafleo.dao.user.UserDaoImpl;
import com.tafleo.pojo.User;
import com.tafleo.view.ViewImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;


public class AllUser extends ViewImpl {

    private JFrame frame;
    private JLabel l1;
    private JTable table;
    private Object[][] l;
    private String[] s;
    private JButton btnNewButton;
    private List<User> userList;
    private JLabel lblNewLabel;
    private JTextPane textPane;
    private Connection connection;
    private UserDao userDao;
    private JButton btnNewButton_1;

    private Object[][] obj;
    private String[] s1;
    private JTable t1;

    public AllUser(List<User> userList, JLabel lblNewLabel, JTable table, Object[][] l, String[] s, JTextPane textPane) {
        this.userList = userList;
        this.lblNewLabel = lblNewLabel;
        this.table = table;
        this.l = l;
        this.s = s;
        this.textPane = textPane;
        //this.connection = connection;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    allUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void allUser() throws Exception {
        connection= BaseDao.getConnection();
        userDao = new UserDaoImpl();
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("全部用户");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WINDOW_ICON_FILE));
        frame.setBounds(100, 100, 455, 353);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        obj = new Object[100][100];
        s1 = new String[]{"TT号", "用户名", "状态"};
        t1 = new JTable();
        userDao.selectAllUser(connection, t1, obj, s1);
        t1.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(43, 48, 363, 217);
        frame.getContentPane().add(scrollPane);
        scrollPane.setViewportView(t1);
        l1 = new JLabel("用户列表");
        l1.setFont(new Font("宋体", Font.BOLD, 18));
        l1.setBounds(178, 17, 76, 21);
        frame.getContentPane().add(l1);
        btnNewButton = new JButton("查询用户信息");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new UserSelect(userList, lblNewLabel, table, l, s, textPane, connection);
            }
        });
        btnNewButton.setBounds(149, 275, 132, 23);
        frame.getContentPane().add(btnNewButton);
        btnNewButton_1 = new JButton("刷新列表");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    userDao.selectAllUser(connection, t1, obj, s1);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(313, 15, 93, 23);
        frame.getContentPane().add(btnNewButton_1);
        frame.setVisible(true);
    }
}
