package com.example.bottomnavigationactivity.model;

import java.io.Serializable;

public class Record implements Serializable {
    private static final long serialVersionUID = 210314663454411973L;

    private String quarter = null;
    private int _id = 0;
    private String volume_of_sms = null;

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getVolume_of_sms() {
        return volume_of_sms;
    }

    public void setVolume_of_sms(String volume_of_sms) {
        this.volume_of_sms = volume_of_sms;
    }
}
