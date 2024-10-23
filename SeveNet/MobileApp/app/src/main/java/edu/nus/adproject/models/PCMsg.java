package edu.nus.adproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.TimeZone;


public class PCMsg implements Parcelable {
    private int id;
    private String imageUrl;
    private String content;
    private int sourceId;
    private String timeStamp;
    private boolean visibility;
    private String status;
    private User user;
    private Tag tag;
    private List<Comment> commentList;

    public PCMsg() {
        commentList = new ArrayList<>();  // Initialize the list here
    }
    public PCMsg(String imageUrl, String content, String timeStamp,
                 boolean visibility, String status, int sourceId) {
        this.imageUrl = imageUrl;
        this.content = content;
        this.timeStamp = timeStamp;
        this.visibility = visibility;
        this.status = status;
        this.sourceId = sourceId;
        this.commentList = new ArrayList<>();
    }

    protected PCMsg(Parcel in) {
        id = in.readInt();
        imageUrl = in.readString();
        content = in.readString();
        sourceId = in.readInt();
        timeStamp = in.readString();
        visibility = in.readByte() != 0;
        status = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        commentList = in.createTypedArrayList(Comment.CREATOR);  // Deserialize the comment list
//        tag = in.readParcelable(Tag.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageUrl);
        dest.writeString(content);
        dest.writeInt(sourceId);
        dest.writeString(timeStamp);
        dest.writeByte((byte) (visibility ? 1 : 0));
        dest.writeString(status);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(commentList);  // Serialize the comment list
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PCMsg> CREATOR = new Creator<PCMsg>() {
        @Override
        public PCMsg createFromParcel(Parcel in) {
            return new PCMsg(in);
        }

        @Override
        public PCMsg[] newArray(int size) {
            return new PCMsg[size];
        }
    };

    // If your timestamp is stored as a String in a specific format, parse it to Date
    public void setTimestampFromString(String timestampStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            this.timeStamp = String.valueOf(sdf.parse(timestampStr));
        } catch (ParseException e) {
            e.printStackTrace();
            this.timeStamp = String.valueOf(new Date());  // Default to the current date if parsing fails
        }
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getTimeStamp() {
        return convertDateFormat(timeStamp);
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
