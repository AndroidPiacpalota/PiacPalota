package com.example.piacpalota.u.i;

public class User {
    private String name;
    private String email;

    // Konstruktor
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getterek
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setterek (ha szükséges)
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}