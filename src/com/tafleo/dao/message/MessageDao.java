package com.tafleo.dao.message;


import com.tafleo.pojo.Message;
import com.tafleo.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface MessageDao {
    //存入用户信息
    public int addMessage(Connection connection, Message message) throws Exception;

    //查询用户消息
    public List<Message> selectMessage(Connection connection, User user, User user1) throws Exception;
}
