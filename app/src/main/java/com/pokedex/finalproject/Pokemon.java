package com.pokedex.finalproject;

public class Pokemon {
    private String name;
    private String type;
    private int weight;
    private int height;
    private String imageUrl;

    public Pokemon(String name, String type, int weight, int height, String imageUrl) {
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.height = height;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
