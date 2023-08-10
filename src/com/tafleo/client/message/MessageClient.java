package com.tafleo.client.message;

import com.tafleo.client.ClientImpl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


//不需要连接服务器
public class MessageClient extends ClientImpl implements Runnable{
    private String toIP;//目标电脑IP地址
    private String fromIP;//发送方电脑IP地址
    private String message;//存放的消息
    private DatagramSocket socket;
    private InetAddress clientToIP;
    private DatagramPacket packet;
    public MessageClient() {
    }

    public MessageClient(String fromIP, String toIP, String message) {
        this.fromIP = fromIP;
        this.toIP = toIP;
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
            clientToIP = InetAddress.getByName(toIP);
            //数据，数据的长度起始，要发送给谁
            String IM;//IP和消息字符串
            IM = fromIP + "#" + message;
            packet = new DatagramPacket(IM.getBytes(), 0, IM.getBytes().length, clientToIP, CLIENT_PORT);
            //3.发送包
            socket.send(packet);
            //关闭流
            //socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
