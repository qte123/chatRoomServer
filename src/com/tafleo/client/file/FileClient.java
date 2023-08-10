package com.tafleo.client.file;

import com.tafleo.client.ClientImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


//客户端
public class FileClient extends ClientImpl implements Runnable {
    private String fileName;//文件名
    private Socket socket;//插座
    private Socket socket1;//插座
    private OutputStream os;//输出流
    private OutputStream os1;//输出流
    private String toIP;//目标电脑IP地址
    private String fromIP;//发送方电脑IP地址
    private String IM;//IP和消息字符串
    private InetAddress clientToIP;
    private File file;
    private InputStream is;
    private String filePath;
    private boolean flag;
    private String morePath;

    public FileClient() {

    }

    public FileClient(String fromIP, String toIP, String fileName, String filePath, boolean flag, String morePath) {
        this.fromIP = fromIP;
        this.toIP = toIP;
        this.fileName = fileName;
        this.filePath = filePath;
        this.flag = flag;
        this.morePath = morePath;
    }

    @Override
    public void run() {
        sendFile();
    }

    public void sendFile() {
        try {
            //1.建立一个Socket 要知道目标电脑地址
            //2.建个包
            //发送给谁
            clientToIP = InetAddress.getByName(toIP);
            //数据，数据的长度起始，要发送给谁
            if (morePath != null) {
                IM = morePath + "^" + flag + "*" + fromIP + "#" + fileName;
            } else {
                IM = "^" + flag + "*" + fromIP + "#" + fileName;
            }
            //3.发送包
            //2.创建一个socket连接
            socket = new Socket(clientToIP, CLIENT_PORT);
            socket1 = new Socket(clientToIP, FILE_TO_PORT);
            //3.发送消息IO流
            file = new File(filePath + "//" + fileName);
            System.out.println(fileName);
            is = new FileInputStream(file);
            os = socket.getOutputStream();
            os1 = socket1.getOutputStream();
            int len;
            byte[] buffer = new byte[MAX];
            os.write(IM.getBytes());
            while ((len = is.read(buffer)) != -1) {
                os1.write(buffer, 0, len);
                os1.flush();
            }
            os1.close();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
