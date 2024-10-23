package edu.nus.adproject.models;

import java.time.LocalDateTime;

public class Following {
    private int id;
    private User followingUser;
    private LocalDateTime followingTime;

    public Following() {}

    public Following(LocalDateTime followingTime, User followingUser) {
        this.followingTime = followingTime;
        this.followingUser = followingUser;
    }

    public User getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    public LocalDateTime getFollowingTime() {
        return followingTime;
    }

    public void setFollowingTime(LocalDateTime followingTime) {
        this.followingTime = followingTime;
    }
}
