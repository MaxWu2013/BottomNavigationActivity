package com.example.bottomnavigationactivity.model;

import java.io.Serializable;

public class Field implements Serializable {
    private static final long serialVersionUID = 139594677913664105L;

    private String type = null;

    private String id = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
