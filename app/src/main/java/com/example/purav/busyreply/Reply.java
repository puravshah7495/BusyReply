package com.example.purav.busyreply;

/**
 * Created by Purav on 2/9/2015.
 */
public class Reply {
    private long id;
    private String reply;

    public Reply(long id, String reply) {
        this.id = id;
        this.reply = reply;
    }

    public Reply() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return reply;
    }
}
