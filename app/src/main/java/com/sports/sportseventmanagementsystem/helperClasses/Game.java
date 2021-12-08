package com.sports.sportseventmanagementsystem.helperClasses;

public class Game {
    private String id,name,image,maxPlayers;

    public Game() {
    }

    public Game(String id, String name, String image, String maxPlayers) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.maxPlayers = maxPlayers;
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

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
