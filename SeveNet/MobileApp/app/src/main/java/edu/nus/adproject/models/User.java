package edu.nus.adproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class User implements Parcelable
{
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String gender;
    private int socialScore;
    private String phoneNum;
    private Auth auth;
    private Role role;
    private String joinDate;
    private String blockList;
    private String country;
    private String status;
    private List<Report> reports;
    private List<Follower> followers;
    private List<Following> followings;
    //private List<BanHistory> banHistories;
    //private List<Notification> notifications;

    public User(){

    }

    public User(String name, String email, Role role, Auth auth, String username, String password, String gender,
                String country, String status, int socialScore, String blockList, String phoneNum, String joinDate) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.auth = auth;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.country = country;
        this.status = status;
        this.socialScore = socialScore;
        this.blockList = blockList;
        this.phoneNum = phoneNum;
        this.joinDate = joinDate;
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        username = in.readString();
        password = in.readString();
        gender = in.readString();
        socialScore = in.readInt();
        phoneNum = in.readString();
        joinDate = in.readString();
        blockList = in.readString();
        country = in.readString();
        status = in.readString();
        // Read lists if they are Parcelable as well
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(gender);
        dest.writeInt(socialScore);
        dest.writeString(phoneNum);
        dest.writeString(joinDate);  // Write as String
        dest.writeString(blockList);
        dest.writeString(country);
        dest.writeString(status);
        // Write lists if they are Parcelable as well
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getBlockList() {
        return blockList;
    }

    public void setBlockList(String blockList) {
        this.blockList = blockList;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSocialScore() {
        return socialScore;
    }

    public void setSocialScore(int socialScore) {
        this.socialScore = socialScore;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Following> getFollowings() {
        return followings;
    }

    public void setFollowings(List<Following> followings) {
        this.followings = followings;
    }

//    public List<BanHistory> getBanHistories() {
//        return banHistories;
//    }
//
//    public void setBanHistories(List<BanHistory> banHistories) {
//        this.banHistories = banHistories;
//    }
//
//    public List<Notification> getNotifications() {
//        return notifications;
//    }
//
//    public void setNotifications(List<Notification> notifications) {
//        this.notifications = notifications;
//    }
}
