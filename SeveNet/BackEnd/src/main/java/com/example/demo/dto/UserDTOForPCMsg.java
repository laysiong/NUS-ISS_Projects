package com.example.demo.dto;

import com.example.demo.model.User;

public class UserDTOForPCMsg {

    // for simplify the return type of PCMsg_User

    private Integer id;

    private String name;

    private String username;


    // Constructor, getters, and setters
    public UserDTOForPCMsg(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
