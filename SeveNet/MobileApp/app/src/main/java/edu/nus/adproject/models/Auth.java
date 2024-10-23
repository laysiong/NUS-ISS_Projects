package edu.nus.adproject.models;

import java.util.List;

public class Auth {
    private int id;
    private String rank;
    private String menuViewJason;
    private List<User> users;

    public Auth() {

    }

    public Auth(String rank, String menuViewJason) {
        this.rank = rank;
        this.menuViewJason = menuViewJason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMenuViewJason() {
        return menuViewJason;
    }

    public void setMenuViewJason(String menuViewJason) {
        this.menuViewJason = menuViewJason;
    }
}
