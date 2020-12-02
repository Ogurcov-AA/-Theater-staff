package com.Classes;

import java.util.ArrayList;

public class UserInfo {

    public static UserInfo currentUser = new UserInfo();
    public static ArrayList<UserInfo> UserList = new ArrayList<>();

    private int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        if(imgPath.trim().equals(""))
            return;
        this.imgPath = imgPath;
    }

    private String imgPath = "/resourse/default.jpg";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getbDay() {
        return bDay;
    }

    public void setbDay(String bDay) {
        this.bDay = bDay;
    }

    public String getRegDay() {
        return regDay;
    }

    public void setRegDay(String regDay) {
        this.regDay = regDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String login;
    private String Name;
    private String Surname;
    private String ip;
    private String country;
    private String mail;
    private String bDay;
    private String regDay;
    private String gender;



}
