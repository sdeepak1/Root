package com.example.root;

public class Notification_Model {
    String Id,Content,Date;

    public Notification_Model(String id, String content, String date) {
        Id = id;
        Content = content;
        Date = date;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
