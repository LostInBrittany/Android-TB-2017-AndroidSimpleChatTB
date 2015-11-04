package org.lostinbrittany.teaching.android.tb.simpletchat.model;

/**
 * Created by horacio on 27/10/15.
 */
public class Message {
    public Message(long timestamp,
                   String author,
                   String message) {
        this.timestamp = timestamp;
        this.author = author;
        this.message = message;
    }

    private long timestamp;
    private String author;
    private String message;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
