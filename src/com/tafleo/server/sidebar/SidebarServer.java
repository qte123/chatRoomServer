package com.tafleo.server.sidebar;

import com.tafleo.pojo.User;
import com.tafleo.server.ServerImpl;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class SidebarServer extends ServerImpl implements Runnable{
    private JTextPane textPane;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress clientToIP;
    private String toIP;//目标电脑IP地址
    private String fromIP;//发送方电脑IP地址
    private String message;//存放的消息
    private List<User> userList;
    private JLabel lblNewLabel;
    private JTable table;
    public SidebarServer(JTextPane textPane, List<User> userList, JLabel lblNewLabel, JTable table) {
        this.textPane = textPane;
        this.userList=userList;
        this.lblNewLabel=lblNewLabel;
        this.table=table;
    }

    @Override
    public void run() {
        try {
            //开放端口
            DatagramSocket socket = new DatagramSocket(MESSAGE_ACCEPT_PORT);
            while (true) {
                //接收数据包
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                //阻塞式接收包裹
                socket.receive(packet);
                //多线程生成独立的会话
                new Thread(new SidebarRunnable(textPane,packet,userList,lblNewLabel,table)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
