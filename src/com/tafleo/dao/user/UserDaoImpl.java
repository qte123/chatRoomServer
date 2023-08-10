package com.tafleo.dao.user;

import com.tafleo.dao.BaseDao;
import com.tafleo.pojo.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class UserDaoImpl implements UserDao {
    @Override
    public int addUser(Connection connection, User user) throws Exception {
        int updateRows = 0;
        PreparedStatement preparedStatement = null;
        String sql = "insert into user (uid,chatNumber,username,password,autograph,gender,phoneNumber,birthday,recentIP,recentTime,chatStatus,friendsChatNumber,home,email,constellation,headPortraitURL,addDate,modifyDate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String uid = user.getUid();
        String chatNumber = user.getChatNumber();
        String username = user.getUsername();
        String password = user.getPassword();
        String autograph = user.getAutograph();
        int gender = user.getGender();
        String phoneNumber = user.getPhoneNumber();
        Date birthday = user.getBirthday();
        String recentIP = user.getRecentIP();
        Date recentTime = user.getRecentTime();
        int chatStatus = user.getChatStatus();
        String friendsChatNumber = user.getFriendChatNumber();
        String home = user.getHome();
        String email = user.getEmail();
        String constellation = user.getConstellation();
        String headPortraitURL = user.getHeadPortraitURL();
        Date addDate = user.getAddDate();
        Date modifyDate = user.getModifyDate();
        Object[] params = {uid, chatNumber, username, password, autograph, gender, phoneNumber, birthday, recentIP, recentTime, chatStatus, friendsChatNumber, home, email, constellation, headPortraitURL, addDate, modifyDate};
        updateRows = BaseDao.execute(connection, preparedStatement, sql, params);
        return updateRows;
    }

    @Override
    public String getNumber(Connection connection, int length) throws Exception {
        String base = "123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 1; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String base1 = "0123456789";
        Random random1 = new Random();
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < length - 1; i++) {
            int number1 = random1.nextInt(base1.length());
            sb1.append(base1.charAt(number1));
        }
        String number = sb.toString() + sb1.toString();
        if (isRepeat(connection, number)) {
            getNumber(connection, length);
        }
        return number;
    }

    @Override
    public boolean isRepeat(Connection connection, String chatNumber) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "select chatNumber from user where chatNumber = ?";
        Object[] param = {chatNumber};
        rs = BaseDao.execute(connection, pstm, rs, sql, param);
        if (rs.next()) {
            return true;
        }
        BaseDao.closeResource(null, pstm, rs);
        return false;
    }

    @Override
    public User getLoginUser(Connection connection, String chatNumber, String password) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from user where chatNumber= ? and password= ?";
            Object[] params = {chatNumber, password};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setUid(rs.getString("uid"));
                user.setChatNumber(rs.getString("chatNumber"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAutograph(rs.getString("autograph"));
                user.setGender(rs.getInt("gender"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                Date time = new Date(rs.getTimestamp("birthday").getTime());
                user.setBirthday(time);
                user.setRecentIP(rs.getString("recentIP"));
                if (rs.getTimestamp("recentTime") != null) {
                    user.setRecentTime(new Date(rs.getTimestamp("recentTime").getTime()));
                }
                user.setChatStatus(rs.getInt("chatStatus"));
                user.setFriendChatNumber(rs.getString("friendsChatNumber"));
                user.setHome(rs.getString("home"));
                user.setEmail(rs.getString("email"));
                user.setConstellation(rs.getString("constellation"));
                user.setHeadPortraitURL(rs.getString("headPortraitURL"));
                Date time2 = new Date(rs.getTimestamp("addDate").getTime());
                user.setAddDate(time2);
                if (rs.getTimestamp("modifyDate") != null) {
                    user.setModifyDate(new Date(rs.getTimestamp("modifyDate").getTime()));
                }
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return user;
    }

    @Override
    //修改用户
    public int modify(Connection connection, User user) throws Exception {
        PreparedStatement pstm = null;
        int flag = 0;
        if (connection != null) {
            String sql = "update user set username= ? ,password= ?,autograph= ?,gender= ?,phoneNumber= ?,birthday= ?,recentIP= ?,recentTime= ?,chatStatus= ?,friendsChatNumber= ? ,home= ?, email= ?,constellation= ?,headPortraitURL= ?,addDate= ?,modifyDate= ? where  chatNumber= ? ";
            Object[] params = {user.getUsername(), user.getPassword(), user.getAutograph(), user.getGender(), user.getPhoneNumber(), user.getBirthday(), user.getRecentIP(), user.getRecentTime(), user.getChatStatus(), user.getFriendChatNumber(), user.getHome(), user.getEmail(), user.getConstellation(), user.getHeadPortraitURL(), user.getAddDate(), user.getModifyDate(), user.getChatNumber()};
            flag = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return flag;
    }

    @Override
    public List<User> getUserList(Connection connection, User user) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = null;
        if (user.getFriendChatNumber() != null && !user.getFriendChatNumber().isEmpty()) {
            String[] users = user.getFriendChatNumber().split(",");
            userList = new ArrayList<>();
            if (connection != null) {
                String sql = "select * from user where chatNumber= ? ";
                for (int i = 0; i < users.length; i++) {
                    Object[] params = {users[i]};
                    rs = BaseDao.execute(connection, pstm, rs, sql, params);
                    if (rs.next()) {
                        user = new User();
                        user.setUid(rs.getString("uid"));
                        user.setChatNumber(rs.getString("chatNumber"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setAutograph(rs.getString("autograph"));
                        user.setGender(rs.getInt("gender"));
                        user.setPhoneNumber(rs.getString("phoneNumber"));
                        Date time = new Date(rs.getTimestamp("birthday").getTime());
                        user.setBirthday(time);
                        user.setRecentIP(rs.getString("recentIP"));
                        if (rs.getTimestamp("recentTime") != null) {
                            user.setRecentTime(new Date(rs.getTimestamp("recentTime").getTime()));
                        }
                        user.setChatStatus(rs.getInt("chatStatus"));
                        user.setFriendChatNumber(rs.getString("friendsChatNumber"));
                        user.setHome(rs.getString("home"));
                        user.setEmail(rs.getString("email"));
                        user.setConstellation(rs.getString("constellation"));
                        user.setHeadPortraitURL(rs.getString("headPortraitURL"));
                        Date time2 = new Date(rs.getTimestamp("addDate").getTime());
                        user.setAddDate(time2);
                        if (rs.getTimestamp("modifyDate") != null) {
                            user.setModifyDate(new Date(rs.getTimestamp("modifyDate").getTime()));
                        }
                        userList.add(user);
                    }
                }
                BaseDao.closeResource(null, pstm, rs);
            }
        }
        return userList;
    }

    @Override
    public void modifyUserList(List<User> list, User user, JLabel lblNewLabel, JTable table, Object[][] l, String[] s) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (user.getChatNumber().equals(list.get(i).getChatNumber())) {
                list.remove(i);
                list.add(i, user);
                break;
            }
        }
        if (list.size() != 0) {
            lblNewLabel.setText(String.valueOf(list.size()));
            for (int i = 0; i < list.size(); i++) {
                l[i][0] = list.get(i).getChatNumber();
                l[i][1] = list.get(i).getUsername();
            }
            DefaultTableModel model = new DefaultTableModel(l, s);
            table.setModel(model);
        }
    }

    @Override
    public void userOnline(List<User> list, JLabel lblNewLabel, JTable table, int chatStatus) throws Exception {
        Object[][] l = new Object[100][100];
        String[] s = new String[]{"TT号", "用户名"};
        Connection connection = BaseDao.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if (connection != null) {
            String sql = "select * from user where chatStatus= ? ";
            Object[] params = {chatStatus};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            while (rs.next()) {
                User user = new User();
                user.setUid(rs.getString("uid"));
                user.setChatNumber(rs.getString("chatNumber"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAutograph(rs.getString("autograph"));
                user.setGender(rs.getInt("gender"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                Date time = new Date(rs.getTimestamp("birthday").getTime());
                user.setBirthday(time);
                user.setRecentIP(rs.getString("recentIP"));
                if (rs.getTimestamp("recentTime") != null) {
                    user.setRecentTime(new Date(rs.getTimestamp("recentTime").getTime()));
                }
                user.setChatStatus(rs.getInt("chatStatus"));
                user.setFriendChatNumber(rs.getString("friendsChatNumber"));
                user.setHome(rs.getString("home"));
                user.setEmail(rs.getString("email"));
                user.setConstellation(rs.getString("constellation"));
                user.setHeadPortraitURL(rs.getString("headPortraitURL"));
                Date time2 = new Date(rs.getTimestamp("addDate").getTime());
                user.setAddDate(time2);
                if (rs.getTimestamp("modifyDate") != null) {
                    user.setModifyDate(new Date(rs.getTimestamp("modifyDate").getTime()));
                }
                userAdd(list, user, lblNewLabel, table, l, s);
            }
        }
        BaseDao.closeResource(connection, pstm, rs);
    }

    @Override
    public void userAdd(List<User> list, User user, JLabel lblNewLabel, JTable table, Object[][] l, String[] s) {
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getChatNumber().equals(user.getChatNumber())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            list.add(user);
        }
        if (list.size() != 0) {
            lblNewLabel.setText(String.valueOf(list.size()));
            for (int i = 0; i < list.size(); i++) {
                l[i][0] = list.get(i).getChatNumber();
                l[i][1] = list.get(i).getUsername();
            }
            DefaultTableModel model = new DefaultTableModel(l, s);
            table.setModel(model);
        }
    }

    @Override
    public void deleteUser(List<User> list, User user, JLabel lblNewLabel, JTable table, Object[][] l, String[] s) {
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                User u = list.get(i);
                if (user.getChatNumber().equals(u.getChatNumber())) {
                    list.remove(i);
                }
            }
        }

        if (list.size() != 0) {
            lblNewLabel.setText(String.valueOf(list.size()));
            for (int i = 0; i < list.size(); i++) {
                l[i][0] = list.get(i).getChatNumber();
                l[i][1] = list.get(i).getUsername();
            }
        } else {
            lblNewLabel.setText(String.valueOf(0));
            l[0][0] = null;
            l[0][1] = null;
        }
        DefaultTableModel model = new DefaultTableModel(l, s);
        table.setModel(model);
    }

    @Override
    public User getUser(Connection connection, String chatNumber) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from user where chatNumber= ? ";
            Object[] params = {chatNumber};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setUid(rs.getString("uid"));
                user.setChatNumber(rs.getString("chatNumber"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAutograph(rs.getString("autograph"));
                user.setGender(rs.getInt("gender"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                Date time = new Date(rs.getTimestamp("birthday").getTime());
                user.setBirthday(time);
                user.setRecentIP(rs.getString("recentIP"));
                if (rs.getTimestamp("recentTime") != null) {
                    user.setRecentTime(new Date(rs.getTimestamp("recentTime").getTime()));
                }
                user.setChatStatus(rs.getInt("chatStatus"));
                user.setFriendChatNumber(rs.getString("friendsChatNumber"));
                user.setHome(rs.getString("home"));
                user.setEmail(rs.getString("email"));
                user.setConstellation(rs.getString("constellation"));
                user.setHeadPortraitURL(rs.getString("headPortraitURL"));
                Date time2 = new Date(rs.getTimestamp("addDate").getTime());
                user.setAddDate(time2);
                if (rs.getTimestamp("modifyDate") != null) {
                    user.setModifyDate(new Date(rs.getTimestamp("modifyDate").getTime()));
                }
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return user;
    }

    @Override
    public User getUser(List<User> list, String chatNumber) {
        User user = null;
        for (int i = 0; i < list.size(); i++) {
            user = list.get(i);
            if (chatNumber.equals(user.getChatNumber())) {
                break;
            }
        }
        return user;
    }

    @Override
    public int userDelete(Connection connection, User user) throws Exception {
        PreparedStatement pstm = null;
        String sql = "delete from user where chatNumber= ? ";
        Object[] params = {user.getChatNumber()};
        int updateRows = BaseDao.execute(connection, pstm, sql, params);
        BaseDao.closeResource(null, pstm, null);
        return updateRows;
    }

    @Override
    public void selectAllUser(Connection connection, JTable table, Object[][] l, String[] s) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();
        User user = null;
        if (connection != null) {
            String sql = "select * from user ";
            Object[] params = {};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            while (rs.next()) {
                user = new User();
                user.setUid(rs.getString("uid"));
                user.setChatNumber(rs.getString("chatNumber"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAutograph(rs.getString("autograph"));
                user.setGender(rs.getInt("gender"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                Date time = new Date(rs.getTimestamp("birthday").getTime());
                user.setBirthday(time);
                user.setRecentIP(rs.getString("recentIP"));
                if (rs.getTimestamp("recentTime") != null) {
                    user.setRecentTime(new Date(rs.getTimestamp("recentTime").getTime()));
                }
                user.setChatStatus(rs.getInt("chatStatus"));
                user.setFriendChatNumber(rs.getString("friendsChatNumber"));
                user.setHome(rs.getString("home"));
                user.setEmail(rs.getString("email"));
                user.setConstellation(rs.getString("constellation"));
                user.setHeadPortraitURL(rs.getString("headPortraitURL"));
                Date time2 = new Date(rs.getTimestamp("addDate").getTime());
                user.setAddDate(time2);
                if (rs.getTimestamp("modifyDate") != null) {
                    user.setModifyDate(new Date(rs.getTimestamp("modifyDate").getTime()));
                }
                list.add(user);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        for (int i = 0; i < list.size(); i++) {
            l[i][0] = list.get(i).getChatNumber();
            l[i][1] = list.get(i).getUsername();
            if (list.get(i).getChatStatus()==0){
                l[i][2] ="离线";
            }else {
                l[i][2] ="在线";
            }
        }
        if (list.size() == 0) {
            l[0][0] = null;
            l[0][1] = null;
            l[0][2] = null;
        }
        for (int i=0;i<list.size();i++){
            System.out.println(l[i][0]+" "+l[i][1]+" "+l[i][2]);
        }
        DefaultTableModel model = new DefaultTableModel(l, s);
        table.setModel(model);
    }
}
