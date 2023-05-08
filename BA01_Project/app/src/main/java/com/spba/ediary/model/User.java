package com.spba.ediary.model;

public class User {

    private static User user = null;
    private String userName;
    private String password;

    private User(String name, String password){

        this.userName = name;
        this.password = password;

    }

    public static User getUser(String name, String password){

        if (user == null){
            return new User(name, password);
        }
        else return user;
    }

    public String getPassword() {
        return password;
    }
}
