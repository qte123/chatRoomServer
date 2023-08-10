package com.tafleo.dao;

import java.io.*;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class BaseDao {
    /*    private static String driver = "com.mysql.jdbc.Driver";
        private static String url = "jdbc:mysql://127.0.0.1:3306/chatroom?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        private static String username = "root";
        private static String password = "123456";*/
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://127.0.0.1:3306/chatroomdb?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    private static String username;
    private static String password;

    /* //静态代码块，类加载的时侯就初始化了
     static {
         Properties properties = new Properties();
         //通过类加载器读取对应的资源
         InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
         try {
             properties.load(is);
             driver = properties.getProperty("driver");
             url = properties.getProperty("url");
             username = properties.getProperty("username");
             password = properties.getProperty("password");
             is.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }*/
    static {
        try {
            File file = new File("res//db");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File("res//db//db.txt");
            if (!file1.exists()) {
                OutputStream os = new FileOutputStream(file1);
                os.write("root,123456".getBytes());
                username = "root";
                password = "123456";
            } else {
                InputStream is = new FileInputStream(file1);
                int len;
                byte[] bytes = new byte[1024];
                String s = "";
                while ((len = is.read(bytes)) != -1) {
                    s = new String(bytes, 0, len);
                }
                String[] s1 = s.split(",");
                username = s1[0];
                password = s1[1];
            }
        } catch (Exception e) {

        }
    }

    /***
     * 获取数据库的连接
     * @return
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /***
     * 编写查询公共类
     * @param connection
     * @param preparedStatement
     * @param resultSet
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static ResultSet execute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, String sql, Object[] params) throws SQLException {
        //预编译的sql,在后面直接执行就可以了
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            //setObject,占位符从1开始，但是我们的数组是从0开始
            preparedStatement.setObject(i + 1, params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    /***
     * 编写增删改公共类
     * @param connection
     * @param preparedStatement
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static int execute(Connection connection, PreparedStatement preparedStatement, String sql, Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            //setObject,占位符从1开始，但是我们的数组是从0开始
            preparedStatement.setObject(i + 1, params[i]);
        }
        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    /***
     * 关闭连接
     * @param connection
     * @param preparedStatement
     * @param resultSet
     * @return
     */
    public static boolean closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        boolean flag = false;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        if (resultSet != null) {
            try {
                resultSet.close();
                //GC回收
                resultSet = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag1 = false;
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                //GC回收
                preparedStatement = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag2 = false;
            }
        }

        if (connection != null) {
            try {
                connection.close();
                //GC回收
                connection = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag3 = false;
            }
        }
        /***
         * 要三个连接关闭成功，才能说明成功
         */
        if (flag1 && flag2 && flag3) {
            flag = true;
        }
        return flag;
    }
}
