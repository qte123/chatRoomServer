package com.tafleo.util;

import com.tafleo.client.action.ActionClient;
import com.tafleo.common.Common;
import com.tafleo.dao.user.UserDaoImpl;
import com.tafleo.pojo.User;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ReFresh extends Common implements Runnable {
    private JLabel lblNewLabel_2;

    public ReFresh(JLabel lblNewLabel_2) {
        this.lblNewLabel_2 = lblNewLabel_2;
    }

    @Override
    public void run() {
        while (true){
            lblNewLabel_2.setText(time());
        }
    }
}
