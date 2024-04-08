package com.pokedex.finalproject;

public class Pokemon {
    private int id;
    private String name;
    private String type;
    private int height;
    private int weight;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.name = type;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
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
}
