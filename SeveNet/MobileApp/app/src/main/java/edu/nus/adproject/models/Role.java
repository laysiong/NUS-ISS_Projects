package edu.nus.adproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Role implements Parcelable {
    private int id;
    private String type;
    private List<User> users;

    public Role() {}

    public Role(int id, String type) {
        this.id = id;
        this.type = type;
    }

    // Parcelable implementation
    protected Role(Parcel in) {
        id = in.readInt();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };

    public Role(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
