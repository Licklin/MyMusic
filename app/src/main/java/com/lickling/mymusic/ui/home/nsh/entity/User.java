package com.lickling.mymusic.ui.home.nsh.entity;

public class User {

    private int id;
    private String userAccount;
    private String userPassword;
    private String userName;

    private String usersex;


    public User() {
    }

    public User(int id, String userAccount, String userPassword, String userName,String usersex) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userName = userName;
        this.usersex = usersex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getusersex() {return usersex;}

    public void setUsersex(String usersex) {this.usersex = usersex;}


}



