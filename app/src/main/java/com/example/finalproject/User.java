package com.example.finalproject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

// Note: Username is UNIQUE and be the ID of corresponding document on firestore.
public class User implements Serializable {

    String _UserName;
    String _UserPassword;
    String _UserFirstname;
    String _UserLastname;
    String _UserDoB;
    String _UserJoinDate;
    String _UserEmail;
    String _UserPhone;
    boolean _UserActive;
    String _UserBio;
    String _UserHobbies;
    String _UserSchool;
    String _UserSpecialization;
    String _UserAvatarUrl;
    ArrayList<String> _UserFriend;
    ArrayList<String> _UserWaitingList;



    public User(){}

    public User(String _UserName, String _UserPassword, String _UserFirstname, String _UserLastname, String _UserDoB, String _UserJoinDate, String _UserEmail, String _UserPhone, boolean _UserActive, String _UserBio, String _UserHobbies, String _UserSchool, String _UserSpecialization) {
        this._UserName = _UserName;
        this._UserPassword = _UserPassword;
        this._UserFirstname = _UserFirstname;
        this._UserLastname = _UserLastname;
        this._UserDoB = _UserDoB;
        this._UserJoinDate = _UserJoinDate;
        this._UserEmail = _UserEmail;
        this._UserPhone = _UserPhone;
        this._UserActive = _UserActive;
        this._UserBio = _UserBio;
        this._UserHobbies = _UserHobbies;
        this._UserSchool = _UserSchool;
        this._UserSpecialization = _UserSpecialization;
    }

    @Override
    public String toString() {
        return "User{" +
                "_UserName='" + _UserName + '\'' +
                ", _UserPassword='" + _UserPassword + '\'' +
                ", _UserFirstname='" + _UserFirstname + '\'' +
                ", _UserLastname='" + _UserLastname + '\'' +
                ", _UserDoB='" + _UserDoB + '\'' +
                ", _UserJoinDate='" + _UserJoinDate + '\'' +
                ", _UserEmail='" + _UserEmail + '\'' +
                ", _UserPhone='" + _UserPhone + '\'' +
                ", _UserActive=" + _UserActive +
                ", _UserBio='" + _UserBio + '\'' +
                ", _UserHobbies='" + _UserHobbies + '\'' +
                ", _UserSchool='" + _UserSchool + '\'' +
                ", _UserFriend='" + _UserFriend + '\'' +
                ", _UserWaitingList='" + _UserWaitingList + '\'' +
                ", _UserSpecialization='" + _UserSpecialization + '\'' +
                '}';
    }

    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public String get_UserPassword() {
        return _UserPassword;
    }

    public void set_UserPassword(String _UserPassword) {
        this._UserPassword = _UserPassword;
    }

    public String get_UserFirstname() {
        return _UserFirstname;
    }

    public void set_UserFirstname(String _UserFirstname) {
        this._UserFirstname = _UserFirstname;
    }

    public String get_UserLastname() {
        return _UserLastname;
    }

    public void set_UserLastname(String _UserLastname) {
        this._UserLastname = _UserLastname;
    }

    public String get_UserDoB() {
        return _UserDoB;
    }

    public void set_UserDoB(String _UserDoB) {
        this._UserDoB = _UserDoB;
    }

    public String get_UserJoinDate() {
        return _UserJoinDate;
    }

    public void set_UserJoinDate(String _UserJoinDate) {
        this._UserJoinDate = _UserJoinDate;
    }

    public String get_UserEmail() {
        return _UserEmail;
    }

    public void set_UserEmail(String _UserEmail) {
        this._UserEmail = _UserEmail;
    }

    public String get_UserPhone() {
        return _UserPhone;
    }

    public void set_UserPhone(String _UserPhone) {
        this._UserPhone = _UserPhone;
    }

    public boolean is_UserActive() {
        return _UserActive;
    }

    public void set_UserActive(boolean _UserActive) {
        this._UserActive = _UserActive;
    }

    public String get_UserBio() {
        return _UserBio;
    }

    public void set_UserBio(String _UserBio) {
        this._UserBio = _UserBio;
    }

    public String get_UserHobbies() {
        return _UserHobbies;
    }

    public void set_UserHobbies(String _UserHobbies) {
        this._UserHobbies = _UserHobbies;
    }

    public String get_UserSchool() {
        return _UserSchool;
    }

    public void set_UserSchool(String _UserSchool) {
        this._UserSchool = _UserSchool;
    }

    public String get_UserSpecialization() {
        return _UserSpecialization;
    }

    public void set_UserSpecialization(String _UserSpecialization) {
        this._UserSpecialization = _UserSpecialization;
    }

    public String get_UserAvatarUrl() {
        return _UserAvatarUrl;
    }

    public void set_UserAvatarUrl(String _UserAvatarUrl) {
        this._UserAvatarUrl = _UserAvatarUrl;
    }

    public int get_UserID() { return 0; }

    public ArrayList<String> get_UserFriend() {
        return _UserFriend;
    }

    public void set_UserFriend(ArrayList<String> _UserFriend) {
        this._UserFriend = _UserFriend;
    }

    public ArrayList<String> get_UserWaitingList() {
        return _UserWaitingList;
    }

    public void set_UserWaitingList(ArrayList<String> _UserWaitingList) {
        this._UserWaitingList = _UserWaitingList;
    }

    public String get_UserAge()

    {   Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int Useryear= Integer.parseInt(this._UserDoB.substring(this._UserDoB.length() - 4));
        return Integer.toString(currentYear-Useryear);
    }
}
