package com.tafleo.view.menu;

import com.tafleo.dao.BaseDao;
import com.tafleo.dao.user.UserDao;
import com.tafleo.dao.user.UserDaoImpl;
import com.tafleo.pojo.User;
import com.tafleo.view.ViewImpl;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserSelect extends ViewImpl {

    private JFrame frame;
    private JTextField textField;
    private JLabel lblNewLabel;
    private JButton btnNewButton;
    private UserDao userDao;
    private Connection connection;
    private List<User> list;
    private JLabel lblNewLabel_1;
    private JTable table;
    private Object[][] l;
    private String[] s;
    private String time;
    private JTextPane textPane;
    public UserSelect(List<User> list,JLabel lblNewLabel_1,JTable table,Object[][] l,String[] s,JTextPane textPane,Connection connection) {
        this.list=list;
        this.lblNewLabel_1=lblNewLabel_1;
        this.table=table;
        this.l=l;
        this.s=s;
        this.textPane=textPane;
        this.connection=connection;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    userSelect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void userSelect() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WINDOW_ICON_FILE));
        frame.setResizable(false);
        frame.setTitle("用户查询");
        frame.setBounds(100, 100, 316, 220);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        lblNewLabel = new JLabel("TT号：");
        lblNewLabel.setBounds(68, 73, 58, 18);
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(126, 72, 110, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        userDao = new UserDaoImpl();

        btnNewButton = new JButton("查询");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String chatNumber = textField.getText();
                    if (chatNumber != null && !chatNumber.isEmpty()) {
                        connection.setAutoCommit(false);
                        User user = userDao.getUser(connection, chatNumber);
                        connection.commit();
                        if (user != null) {
                            new Information(user,list,lblNewLabel_1,table,l,s,textPane);
                        } else {
                            new Error();
                        }
                    } else {
                        new Error();
                    }
                } catch (Exception e1) {
                    try {
                        connection.rollback();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    e1.printStackTrace();
                }

            }
        });
        btnNewButton.setBounds(96, 129, 118, 23);
        frame.getContentPane().add(btnNewButton);
        frame.setVisible(true);
    }
}
