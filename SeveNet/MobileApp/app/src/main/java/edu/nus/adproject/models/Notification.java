package edu.nus.adproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Notification implements Parcelable {
    private int id;
    private User notificationUser;
    private String title;
    private String message;
    private String notificationTime;
    private NotificationStatus notificationStatus;

    public Notification() {}

    public Notification(String title, String message, String notificationTime, NotificationStatus notificationStatus) {
        this.title = title;
        this.message = message;
        this.notificationTime = notificationTime;
        this.notificationStatus = notificationStatus;
    }

    protected Notification(Parcel in) {
        id = in.readInt();
        title = in.readString();
        message = in.readString();
        notificationTime = in.readString();
        notificationUser = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(notificationTime);
        dest.writeParcelable(notificationUser, flags);
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


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getNotificationUser() {
        return notificationUser;
    }

    public void setNotificationUser(User notificationUser) {
        this.notificationUser = notificationUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationTime() {
        return convertDateFormat(notificationTime);
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
