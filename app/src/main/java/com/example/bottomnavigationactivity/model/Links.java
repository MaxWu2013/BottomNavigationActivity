package com.example.bottomnavigationactivity.model;

import java.io.Serializable;

public class Links implements Serializable {

    private String start = null;
    private String next = null;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
