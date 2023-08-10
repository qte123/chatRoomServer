package com.tafleo.server.action;

import com.tafleo.pojo.User;
import com.tafleo.server.ServerImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class ActionServer extends ServerImpl implements Runnable {
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
    public ActionServer(JTextPane textPane,List<User> userList,JLabel lblNewLabel,JTable table) {
        this.textPane = textPane;
        this.userList=userList;
        this.lblNewLabel=lblNewLabel;
        this.table=table;
    }

    @Override
    public void run() {
        try {
            //开放端口
            DatagramSocket socket = new DatagramSocket(ACCEPT_PORT);
            while (true) {
                //接收数据包
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                //阻塞式接收包裹
                socket.receive(packet);
                //多线程生成独立的会话
                new Thread(new ActionRunnable(textPane,packet,userList,lblNewLabel,table)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}