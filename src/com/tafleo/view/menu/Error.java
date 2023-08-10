package com.tafleo.view.menu;

import com.tafleo.view.ViewImpl;

import java.awt.*;

import javax.swing.*;

public class Error extends ViewImpl {

    private JFrame frame;
    private JLabel lblNewLabel;

    public Error() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    error();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void error() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("错误警告");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WINDOW_ICON_FILE));
        frame.setBounds(100, 100, 305, 194);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        lblNewLabel = new JLabel();
        lblNewLabel.setText("没有查到指定用户");
        lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel.setBounds(74, 70, 177, 19);
        frame.getContentPane().add(lblNewLabel);
        frame.setVisible(true);
    }

}