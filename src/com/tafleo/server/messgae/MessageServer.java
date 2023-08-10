package com.tafleo.server.messgae;

import com.tafleo.pojo.User;
import com.tafleo.server.ServerImpl;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

//还是要等待客户端的连接！
public class MessageServer extends ServerImpl implements Runnable {
    private JTextPane textPane;
    private List<User> list;

    public MessageServer(JTextPane textPane,List<User> list) {
        this.textPane = textPane;
        this.list=list;
    }

    @Override
    public void run() {
        try {
            //开放端口
            DatagramSocket socket = new DatagramSocket(SERVER_PORT);
            while (true) {
                //接收数据包
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                //阻塞式接收包裹
                socket.receive(packet);
                //多线程生成独立的会话
                new Thread(new MessageServerRunnable(packet, textPane,list)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //关闭连接
    //socket.close();

}
