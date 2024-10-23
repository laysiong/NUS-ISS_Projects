package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.interfacemethods.FollowInterface;
import com.example.demo.model.FollowList;

import com.example.demo.model.User;
import com.example.demo.repository.FollowListRepository;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional (readOnly = true)
public class FollowService implements FollowInterface {


    @Autowired 
    private FollowListRepository followListRepository;
    
    
    @Override
    public List<User> getFollowers(User user) {
    	 //filter out block user
    	List<Integer> blockedUserIds = user.getBlockedUserIds();

    	 List<FollowList> followers = followListRepository.findByFollowedUser(user);
         return followers.stream()
                         .map(FollowList::getFollowingUser)
                         .filter(follower -> !blockedUserIds.contains(follower.getId()))
                         .collect(Collectors.toList());
    }

    @Override
    public List<User> getFollowings(User user) {
    	
    	List<Integer> blockedUserIds = user.getBlockedUserIds();
		
    	List<FollowList> followings = followListRepository.findByFollowingUser(user);
        return followings.stream()
                         .map(FollowList::getFollowedUser)
                         .filter(follower -> !blockedUserIds.contains(follower.getId()))
                         .collect(Collectors.toList());
    }
    
        
    public boolean isFollowing(User currentUser, User targetUser) {
        return followListRepository.existsByFollowingUserAndFollowedUser(currentUser, targetUser);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void followUser(User follower, User followedUser) {
    	
       if (!isFollowing(follower, followedUser)) {
          FollowList followList = new FollowList();
          followList.setFollowingUser(follower);
          followList.setFollowedUser(followedUser);
          followList.setFollowedTime(LocalDateTime.now());
          followListRepository.save(followList);
    	}
    }
    

    @Override
    @Transactional(readOnly = false)
    public void unfollowUser(User follower, User followedUser) {
    	followListRepository.deleteByFollowingUserAndFollowedUser(follower, followedUser);

    }

}
