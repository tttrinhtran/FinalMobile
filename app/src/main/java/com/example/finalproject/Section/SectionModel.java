package com.example.finalproject.Section;

import java.util.ArrayList;
import java.util.Date;

public class SectionModel {
    String _SectionDate;

    String _SectionHour;
    String _SectionName;
    String _SectionDescript;
    String _SectionHost;



    ArrayList<String>_SectionParticipate;

    public SectionModel(String _SectionDate, String _SectionHour,String _SectionName, String _SectionDescript, String _SectionHost) {
        this._SectionDate = _SectionDate;
        this._SectionName = _SectionName;
        this._SectionDescript = _SectionDescript;
        this._SectionHost = _SectionHost;
        this._SectionHour=_SectionHour;
    }

    public String toString() {
        return "Section{" +
                "_SectionDate='" +_SectionDate + '\'' +
                ", _SectionName='" + _SectionName + '\'' +
                ", _SectionDescript='" + _SectionDescript + '\'' +
                ", _SectionParticipate='" + _SectionParticipate + '\'' +
                '}';
    }
    public String get_SectionDate() {
        return _SectionDate;
    }

    public void set_SectionDate(String _SectionDate) {
        this._SectionDate = _SectionDate;
    }

    public String get_SectionDescript() {
        return _SectionDescript;
    }

    public void set_SectionDescript(String _SectionDescript) {
        this._SectionDescript = _SectionDescript;
    }

    public String get_SectionHost() {
        return _SectionHost;
    }

    public void set_SectionHost(String _SectionHost) {
        this._SectionHost = _SectionHost;
    }

    public ArrayList<String> get_SectionParticipate() {
        return _SectionParticipate;
    }

    public void set_SectionParticipate(ArrayList<String> _SectionParticipate) {
        this._SectionParticipate = _SectionParticipate;
    }

    public String get_SectionName() {
        return _SectionName;
    }

    public void set_SectionName(String _SectionName) {
        this._SectionName = _SectionName;
    }

    public String get_SectionHour() {
        return _SectionHour;
    }

    public void set_SectionHour(String _SectionHour) {
        this._SectionHour = _SectionHour;
    }
}
