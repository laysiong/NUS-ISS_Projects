package edu.nus.adproject.models;

import java.time.LocalDateTime;

public class Follower {
    private int id;
    private User followedUser;
    private LocalDateTime followedTime;

    public Follower() {}

    public Follower(LocalDateTime followedTime, User followedUser) {
        this.followedTime = followedTime;
        this.followedUser = followedUser;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public LocalDateTime getFollowedTime() {
        return followedTime;
    }

    public void setFollowedTime(LocalDateTime followedTime) {
        this.followedTime = followedTime;
    }
}
