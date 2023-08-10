package com.tafleo.server.file;

import com.tafleo.server.ServerImpl;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//服务器
public class FileServer extends ServerImpl implements Runnable {
    private JTextPane textPane;

    public FileServer(JTextPane textPane) {
        this.textPane = textPane;
    }

    @Override
    public void run() {
        try {
            //1.我有一个地址
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("-------------------------------------------服务器控制台-------------------------------------------");
            //2.等待客户端连接过来
            while (true) {
                //3.多线程生成独立的会话
                Socket socket = serverSocket.accept();
                new Thread(new FileServerRunnable(socket, textPane)).start();
                System.out.println(Thread.currentThread().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           /* //关闭资源
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}
