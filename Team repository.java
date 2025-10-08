package com.example.alerting.repository;

import com.example.alerting.model.Team;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TeamRepository {
    private final Map<String, Team> teams = new ConcurrentHashMap<>();
    public void save(Team t){ teams.put(t.getId(), t); }
    public Optional<Team> findById(String id){ return Optional.ofNullable(teams.get(id)); }
    public List<Team> findAll(){ return new ArrayList<>(teams.values()); }
}
