package com.example.demo.interfacemethods;

import java.util.List;

import com.example.demo.model.User;

public interface FollowInterface {
    void followUser(User follower, User followedUser);
    void unfollowUser(User follower, User followedUser);
    boolean isFollowing(User currentUser, User targetUser);
    
	List<User> getFollowers(User user);
	List<User> getFollowings(User user);
}