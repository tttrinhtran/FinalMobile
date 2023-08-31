package com.example.finalproject;

public class User {
    String _username;
    String _password;
    String _firstname;
    String _lastname;
    String _dob;
    String _join_date;
    String _email;
    String _phone;

    public User(String _username, String _password, String _firstname, String _lastname, String _dob, String _join_date, String _email, String _phone) {
        this._username = _username;
        this._password = _password;
        this._firstname = _firstname;
        this._lastname = _lastname;
        this._dob = _dob;
        this._join_date = _join_date;
        this._email = _email;
        this._phone = _phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "_username='" + _username + '\'' +
                ", _password='" + _password + '\'' +
                ", _firstname='" + _firstname + '\'' +
                ", _lastname='" + _lastname + '\'' +
                ", _dob='" + _dob + '\'' +
                ", _join_date='" + _join_date + '\'' +
                ", _email='" + _email + '\'' +
                ", _phone='" + _phone + '\'' +
                '}';
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_firstname() {
        return _firstname;
    }

    public void set_firstname(String _firstname) {
        this._firstname = _firstname;
    }

    public String get_lastname() {
        return _lastname;
    }

    public void set_lastname(String _lastname) {
        this._lastname = _lastname;
    }

    public String get_dob() {
        return _dob;
    }

    public void set_dob(String _dob) {
        this._dob = _dob;
    }

    public String get_join_date() {
        return _join_date;
    }

    public void set_join_date(String _join_date) {
        this._join_date = _join_date;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }
}
