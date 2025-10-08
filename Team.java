package com.example.alerting.model;

import java.util.UUID;

public class Team {
    private final String id = UUID.randomUUID().toString();
    private String name;
    public Team(String name) { this.name = name; }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
