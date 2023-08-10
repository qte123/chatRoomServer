package com.tafleo.common;

public class ServerBase extends Common {
    //传输最大字节
    public static final int MAX = 1024;
    //服务器文件发送端口号
    public static final int FILE_TO_PORT = 9998;
    //服务器文件接收端口号
    public static final int FILE_FROM_PORT = 9997;
    //服务器文字端口号
    public static final int SERVER_PORT = 8888;
    //客户端端口号
    public static final int CLIENT_PORT = 9999;
    //注册信息接收端口
    public static final int ACCEPT_PORT = 9995;
    //注册信息发送端口
    public static final int SEND_PORT = 9996;
    //本地客户端用户信息发送端口号
    public static final int MESSAGE_SEND_PORT = 8878;
    //本地客户端用户信息接收端口号
    public static final int MESSAGE_ACCEPT_PORT = 8877;
    //手机号码正则表达式
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    //电子邮箱正则表达式
    public static final String REGEX_MAIL = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
    //日期的正则表达式
    public static final String REGEX_DATETIME = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
}
