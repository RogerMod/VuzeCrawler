package org.crawler.model;


public class VuzeMsg {
    
    int id;
    public String type;
    public String timestamp;
    public String data;

    public VuzeMsg() {
        this.type = "";
        this.timestamp = "";
        this.data = "";
    }

    public VuzeMsg(int id, String type, String timestamp, String data) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.data = data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setData(String data) {
        this.data = data;
    }


    
}