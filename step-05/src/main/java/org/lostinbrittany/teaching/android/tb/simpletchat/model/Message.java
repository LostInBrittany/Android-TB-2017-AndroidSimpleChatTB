package org.lostinbrittany.teaching.android.tb.simpletchat.model;


public class Message {

    private long date;
    private String username;
    private String message;


    public Message(long date, String username, String message) {
        this.username = username;
        this.date = date;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
