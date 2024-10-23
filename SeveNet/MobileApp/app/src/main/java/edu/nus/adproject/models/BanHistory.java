package edu.nus.adproject.models;

import java.time.LocalDateTime;

public class BanHistory {
    private int id;
    private User banUser;
    private LocalDateTime banTime;
    private int banDuration;

    public BanHistory() {}

    public BanHistory(LocalDateTime banTime, int banDuration) {
        this.banTime = banTime;
        this.banDuration = banDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getBanUser() {
        return banUser;
    }

    public void setBanUser(User banUser) {
        this.banUser = banUser;
    }

    public LocalDateTime getBanTime() {
        return banTime;
    }

    public void setBanTime(LocalDateTime banTime) {
        this.banTime = banTime;
    }

    public int getBanDuration() {
        return banDuration;
    }

    public void setBanDuration(int banDuration) {
        this.banDuration = banDuration;
    }
}
