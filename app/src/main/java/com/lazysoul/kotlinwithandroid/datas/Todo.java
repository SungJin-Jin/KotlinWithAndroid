package com.lazysoul.kotlinwithandroid.datas;

import java.util.Date;

/**
 * Created by Lazysoul on 2017. 7. 9..
 */

public class Todo {

    private int id;

    private String body;

    private boolean isChecked;

    private Date createdAt;

    private boolean isFixed = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }
}
