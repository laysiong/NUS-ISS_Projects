package edu.nus.adproject.models;

public class Like {
    private int id;
    private int userId;
    private int pcmsgId;

    public Like() {}

    public Like(int userId, int pcmsgId) {
        this.userId = userId;
        this.pcmsgId = pcmsgId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPcmsgId() {
        return pcmsgId;
    }

    public void setPcmsgId(int pcmsgId) {
        this.pcmsgId = pcmsgId;
    }
}
