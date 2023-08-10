package com.tafleo.dao.message;

import com.tafleo.dao.BaseDao;
import com.tafleo.pojo.Message;
import com.tafleo.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    @Override
    public int addMessage(Connection connection, Message message) throws Exception {
        int updateRows = 0;
        PreparedStatement preparedStatement = null;
        String sql = "insert into message (uid,message,fromChatNumber,toChatNumber,date,ifDelete,type,addDate) values (?,?,?,?,?,?,?,?)";
        String uid = message.getUid();
        String message1 = message.getMessage();
        String fromChatNumber = message.getFromChatNumber();
        String toChatNumber = message.getToChatNumber();
        Date date = message.getDate();
        int ifDelete = message.getIfDelete();
        int type = message.getType();
        Date addDate = message.getAddDate();
        Object[] params = {uid, message1, fromChatNumber, toChatNumber, date, ifDelete, type, addDate};
        updateRows = BaseDao.execute(connection, preparedStatement, sql, params);
        return updateRows;
    }

    @Override
    public List<Message> selectMessage(Connection connection, User user, User user1) throws Exception {
        PreparedStatement pstm = null;
        PreparedStatement pstm1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List<Message> messageList = new ArrayList<>();
        Message message = null;
        Message message1 = null;
        if (connection != null) {
            String sql = "select * from message where fromChatNumber = ? and toChatNumber = ? ORDER BY Date ASC";
            Object[] params = {user.getChatNumber(), user1.getChatNumber()};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            while (rs.next()) {
                message = new Message();
                message.setUid(rs.getString("uid"));
                message.setMessage(rs.getString("message"));
                message.setFromChatNumber(rs.getString("fromChatNumber"));
                message.setToChatNumber(rs.getString("toChatNumber"));
                Date time = new Date(rs.getTimestamp("date").getTime());
                message.setDate(time);
                message.setIfDelete(rs.getInt("ifDelete"));
                message.setType(rs.getInt("type"));
                Date time1 = new Date(rs.getTimestamp("addDate").getTime());
                message.setAddDate(time1);
                messageList.add(message);
            }
            Object[] params1 = {user1.getChatNumber(), user.getChatNumber()};
            rs1 = BaseDao.execute(connection, pstm1, rs1, sql, params1);
            while (rs1.next()) {
                message1 = new Message();
                message1.setUid(rs1.getString("uid"));
                message1.setMessage(rs1.getString("message"));
                message1.setFromChatNumber(rs1.getString("fromChatNumber"));
                message1.setToChatNumber(rs1.getString("toChatNumber"));
                Date time = new Date(rs1.getTimestamp("date").getTime());
                message1.setDate(time);
                message1.setIfDelete(rs1.getInt("ifDelete"));
                message1.setType(rs1.getInt("type"));
                Date time1 = new Date(rs1.getTimestamp("addDate").getTime());
                message1.setAddDate(time1);
                messageList.add(message1);
            }
            BaseDao.closeResource(null, pstm, rs);
            BaseDao.closeResource(null, pstm1, rs1);
        }
        return messageList;
    }
}