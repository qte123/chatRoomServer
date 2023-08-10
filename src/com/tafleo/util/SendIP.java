package com.tafleo.util;

import com.tafleo.client.action.ActionClient;
import com.tafleo.common.Common;

import java.net.InetAddress;

public class SendIP extends Common implements Runnable {
    public SendIP() {
    }

    @Override
    public void run() {
        while (true){
            InetAddress localIP = null;//获取本机IP地址
            try {
                localIP = InetAddress.getLocalHost();
                String ip = localIP.getHostAddress();
                Thread.sleep(4000);
                new Thread(new ActionClient("255.255.255.255", "serverIP*" + ip)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
