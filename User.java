package com.example.alerting.model;

import java.util.UUID;

public class User {
    private final String id = UUID.randomUUID().toString();
    private String name;
    private String teamId;
    private String email;
    public User(String name, String teamId, String email){
        this.name = name; this.teamId = teamId; this.email = email;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getTeamId() { return teamId; }
    public String getEmail() { return email; }
    public void setName(String name) { this.name = name; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
}
