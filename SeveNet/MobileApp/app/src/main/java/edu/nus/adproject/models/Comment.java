package edu.nus.adproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Comment implements Parcelable {
    private int id;
    private int source_id;
    private String content;
    private String user_name;
    private int user_id;
    private String timeStamp;
    private String status;
    private String likes;
    private List<Comment> replies; // This will hold the nested comments

    public Comment() {
        this.replies = new ArrayList<>(); // Initialize the replies list
    }

    public Comment(int id, String username, int user_id, String content, String timestamp, String status) {
        this.id = id;
        this.user_name = username;
        this.user_id = user_id;
        this.content = content;
        this.timeStamp = timestamp;
        this.status = status;
    }

    protected Comment(Parcel in) {
        id = in.readInt();
        user_name = in.readString();
        user_id = in.readInt();
        content = in.readString();
        timeStamp = in.readString();
        status = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(user_name);
        parcel.writeInt(user_id);
        parcel.writeString(content);
        parcel.writeString(timeStamp);
        parcel.writeString(status);
    }

    private String convertDateFormat(String originalDateStr) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = originalFormat.parse(originalDateStr);

            // Format the Date object into the desired format
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM d, yyyy 'at' HH:mm", Locale.ENGLISH);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return originalDateStr; // Return the original string in case of a parsing error
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getuser_id(){ return  user_id;}

    public void setUser_id(int user_id){this.user_id= user_id;}

    public String getuser_name(){ return user_name;}

    public void setuser_name(String user_name){this.user_name= user_name;}

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return user_name;
    }

    public void setUser_id(String user_name) {
        this.user_name = user_name;
    }

    public String getTimeStamp() {
        return convertDateFormat(timeStamp);
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

}

