package com.tafleo.common;

import com.tafleo.common.ServerBase;

import java.util.Calendar;
import java.util.regex.Pattern;

//共同类
public class Common {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public String time() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        String hours = ifSmall(hour);
        String minutes = ifSmall(minute);
        String seconds = ifSmall(second);
        String time = year + "年" + month + "月" + day + "日 " + hours + ":" + minutes + ":" + seconds;
        return time;
    }

    //判断是否小于9
    public String ifSmall(int number) {
        return number <= 9 ? "0" + number : "" + number;
    }

    //判断是否为电子邮箱
    public boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches(ServerBase.REGEX_MAIL, email);
        }
        return false;
    }

    //判断是否为电话号码
    public boolean isValidPhoneNumber(String phoneNumber) {
        if ((phoneNumber != null) && (!phoneNumber.isEmpty())) {
            return Pattern.matches(ServerBase.REGEX_MOBILE, phoneNumber);
        }
        return false;
    }
    //判断是否为正确日期格式
    public boolean isValidDatetime(String Datetime) {
        if ((Datetime != null) && (!Datetime.isEmpty())) {
            return Pattern.matches(ServerBase.REGEX_DATETIME, Datetime);
        }
        return false;
    }

    //去除字符串前后面空格
    public String textTrim(String textContent) {
        textContent = textContent.trim();
        while (textContent.startsWith("　")) {//这里判断是不是全角空格
            textContent = textContent.substring(1, textContent.length()).trim();
        }
        while (textContent.endsWith("　")) {
            textContent = textContent.substring(0, textContent.length() - 1).trim();
        }
        return textContent;
    }

    //HTML转text
    public String getHtmlText(String html) {
        String textContent = html.substring(html.indexOf("<body>") + 6, html.indexOf("</body>"));
        return textTrim(textContent);
    }

    //text转html
    public String getHtml(String text) {
        String start = "<html><head></head><body>";
        String end = "</body><ml>";
        return start + text + end;
    }
}
