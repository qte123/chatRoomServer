package com.tafleo.dao.user;

import com.tafleo.pojo.User;

import javax.swing.*;
import java.sql.Connection;
import java.util.List;

public interface UserDao {
    //存入用户信息
    public int addUser(Connection connection, User user) throws Exception;

    //生成用户账号
    public String getNumber(Connection connection, int length) throws Exception;

    //查询生成的TT号是否已经重复
    public boolean isRepeat(Connection connection, String chatNumber) throws Exception;

    //查询用户
    public User getUser(Connection connection, String chatNumber) throws Exception;

    //得到登录的用户
    public User getLoginUser(Connection connection, String chatNumber, String password) throws Exception;

    //修改用户
    public int modify(Connection connection, User user) throws Exception;

    //获取用户列表
    public List<User> getUserList(Connection connection, User user) throws Exception;

    //修改用户列表
    public void modifyUserList(List<User> list, User user, JLabel lblNewLabel, JTable table, Object[][] l, String[] s) throws Exception;

    //读取在线用户
    public void userOnline(List<User> list, JLabel lblNewLabel, JTable table, int chatStatus) throws Exception;

    //将用户添加到服务器列表
    public void userAdd(List<User> list, User user, JLabel lblNewLabel, JTable table, Object[][] l, String[] s);

    //通过账号查找用户
    public User getUser(List<User> list, String chatNumber);

    //删除用户
    public void deleteUser(List<User> list, User user, JLabel lblNewLabel, JTable table, Object[][] l, String[] s);

    //从数据库删除用户
    public int userDelete(Connection connection, User user) throws Exception;

    //查询所有用户
    public void selectAllUser(Connection connection,JTable table, Object[][] l, String[] s) throws Exception;
}
