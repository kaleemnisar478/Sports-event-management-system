package com.sports.sportseventmanagementsystem.helperClasses;

public class Player {
    private String id,name,image,email;

    public Player() {
    }

    public Player(String id, String name, String image, String email) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
