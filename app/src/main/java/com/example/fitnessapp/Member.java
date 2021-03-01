package com.example.fitnessapp;

public class Member {
    private String name;
    private String email;
    private int height;
    private int weight;

    public  Member(){

    }

    public Member(String name, String email, int height, int weight){
        this.name = name;
        this.email = email;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
