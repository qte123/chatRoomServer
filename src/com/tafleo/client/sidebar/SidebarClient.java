package com.tafleo.client.sidebar;

import com.tafleo.client.ClientImpl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SidebarClient extends ClientImpl implements Runnable{
    private String toIP;//目标电脑IP地址
    private String fromIP;//发送方电脑IP地址
    private String message;//存放的消息
    private DatagramSocket socket;
    private InetAddress clientToIP;
    private DatagramPacket packet;
    public SidebarClient() {
    }

    public SidebarClient(String fromIP, String message) {
        this.fromIP = fromIP;
        this.message = message;
    }

    @Override
    public void run() {
        sendMessage();
    }

    @Override
    public void sendMessage() {
        try {
            //1.建立一个Socket
            socket = new DatagramSocket();
            //2.建个包
            //发送给谁
            clientToIP = InetAddress.getByName(fromIP);
            //数据，数据的长度起始，要发送给谁
            String IM;//IP和消息字符串
            IM = fromIP + "#" + message;
            packet = new DatagramPacket(IM.getBytes(), 0, IM.getBytes().length, clientToIP, MESSAGE_SEND_PORT);
            //3.发送包
            socket.send(packet);
            //关闭流
            //socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
