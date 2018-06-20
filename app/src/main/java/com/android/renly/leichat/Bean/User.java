package com.android.renly.leichat.Bean;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String headPhoto;
    private String password;
    private List<User>friends;

    //朋友列表初始化
    public User(String name,String headPhoto){
        this.name = name;
        this.headPhoto = headPhoto;
    }

    //账号的初始化
    public User(String name, String password, String headPhoto){
        this.name = name;
        this.password = password;
        this.headPhoto = headPhoto;
    }

    public User(){
        super();
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
