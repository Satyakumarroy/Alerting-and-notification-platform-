package com.example.alerting.repository;

import com.example.alerting.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    public void save(User u){ users.put(u.getId(), u); }
    public Optional<User> findById(String id){ return Optional.ofNullable(users.get(id)); }
    public List<User> findAll(){ return new ArrayList<>(users.values()); }
    public List<User> findByTeamId(String teamId){
        List<User> list = new ArrayList<>();
        users.values().forEach(u -> { if (teamId.equals(u.getTeamId())) list.add(u); });
        return list;
    }
}
