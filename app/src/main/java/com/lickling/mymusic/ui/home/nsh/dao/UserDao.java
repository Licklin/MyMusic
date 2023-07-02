package com.lickling.mymusic.ui.home.nsh.dao;


import android.util.Log;
import android.widget.Toast;


import com.lickling.mymusic.ui.home.nsh.entity.User;
import com.lickling.mymusic.ui.home.nsh.presenter.JDBCUtils;
import com.lickling.mymusic.ui.setting.password.PassWordActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class UserDao {

    private static final String TAG = "mysql-party-UserDao";

    /**
     * function: 登录
     * */
    public int login(String userAccount, String userPassword){

        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();
        int msg = 0;
        try {
            // mysql简单的查询语句。这里是根据user表的userAccount字段来查询某条记录
            String sql = "select * from music_user where userAccount = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    Log.e(TAG,"账号：" + userAccount);
                    //根据账号进行查询
                    ps.setString(1, userAccount);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    int count = rs.getMetaData().getColumnCount();
                    //将查到的内容储存在map里
                    while (rs.next()){
                        // 注意：下标是从1开始的
                        for (int i = 1;i <= count;i++){
                            String field = rs.getMetaData().getColumnName(i);
                            map.put(field, rs.getString(field));
                        }
                    }
                    connection.close();
                    ps.close();

                    if (map.size()!=0){
                        StringBuilder s = new StringBuilder();
                        //寻找密码是否匹配
                        for (String key : map.keySet()){
                            if(key.equals("userPassword")){
                                if(userPassword.equals(map.get(key))){
                                    msg = 1;            //密码正确
                                }
                                else
                                    msg = 2;            //密码错误
                                break;
                            }
                        }
                    }else {
                        Log.e(TAG, "查询结果为空");
                        msg = 3;
                    }
                }else {
                    msg = 0;
                }
            }else {
                msg = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常login：" + e.getMessage());
            msg = 0;
        }
        return msg;
    }


    /**
     * function: 注册
     * */
    public boolean register(User user){
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "insert into music_user(userAccount,userName,userPassword,usersex) values (?,?,?,?)";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){

                    //将数据插入数据库
                    ps.setString(1,user.getUserAccount());
                    ps.setString(2,user.getUserName());
                    ps.setString(3,user.getUserPassword());
                    ps.setString(4,user.getusersex());

                    System.out.println("用户账号:"+user.getUserAccount());
                    System.out.println("用户密码:"+user.getUserPassword());
                    System.out.println("用户名:"+user.getUserName());


                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    if(rs>0)
                        return true;
                    else
                        return false;
                }else {
                    return  false;
                }
            }else {
                return  false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
            return false;
        }

    }

    /**
     * function: 根据账号进行查找该用户是否存在
     * */
    public User findUser(String userAccount) {

        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();
        User user = null;
        try {
            String sql = "select * from music_user where userAccount = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        //注意：下标是从1开始
                        int id = rs.getInt(1);
                        String userAccount1 = rs.getString(2);
                        String userName = rs.getString(3);
                        String userPassword = rs.getString(4);
                        String usersex = rs.getString(5);

                        user = new User(id, userAccount1, userName,userPassword,usersex);
                        System.out.println("id:"+id);
                        System.out.println("iuserAccount1:"+userAccount1);
                        System.out.println("userName:"+userName);
                        System.out.println("userPassword:"+userPassword);
                        System.out.println("usersex:"+usersex);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常findUser：" + e.getMessage());
            return null;
        }
        return user;
    }

    /**
     * function: 根据账号进行删除用户
     * */
    public boolean deleteUser(String userAccount) {

        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "delete from music_user where userAccount = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {

                    // 设置查询参数
                    ps.setString(1, userAccount);

                    // 执行删除操作并返回结果
                    int rs = ps.executeUpdate();
                    if (rs > 0)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常deleteUser：" + e.getMessage());
            return false;
        }
    }

    /**
     * function: 修改密码
     */
    public boolean modifyPassword(String userAccount, String oldPassword, String newPassword) {


        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {

            // 先根据账号和旧密码查询用户是否存在
            User user = findUser(userAccount);
            if (user == null || !user.getUserName().equals(oldPassword)) {
                // 用户不存在或旧密码不匹配
                System.out.println("密码不匹配");
                return false;
            }
            System.out.println("user.getUserName()："+user.getUserName());
            System.out.println("oldPassword："+oldPassword);
            // 更新密码
            String sql = "update music_user set userPassword = ? where userAccount = ?";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);

                if (ps != null) {

                    // 设置查询参数
                    ps.setString(1, newPassword);
                    ps.setString(2, userAccount);

                    // 执行更新操作并返回结果
                    int rs = ps.executeUpdate();
                    System.out.println("执行 SQL 语句：" + sql);
                    System.out.println("受影响的行数：" + rs);
                    if (rs > 0) {
                        System.out.println("修改数据库");
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常modifyPassword：" + e.getMessage());
            return false;
        }
    }



}
