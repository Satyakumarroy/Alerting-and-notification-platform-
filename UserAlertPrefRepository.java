package com.example.alerting.repository;

import com.example.alerting.model.UserAlertPreference;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class UserAlertPrefRepository {
    // key: alertId + "|" + userId
    private final Map<String, UserAlertPreference> store = new ConcurrentHashMap<>();
    private String key(long alertId, String userId){ return alertId + "|" + userId; }
    public UserAlertPreference getOrCreate(long alertId, String userId){
        return store.computeIfAbsent(key(alertId, userId), k -> new UserAlertPreference(alertId, userId));
    }
    public Optional<UserAlertPreference> find(long alertId, String userId){
        return Optional.ofNullable(store.get(key(alertId, userId)));
    }
    public List<UserAlertPreference> findByUser(String userId){
        return store.values().stream().filter(p -> p.getUserId().equals(userId)).collect(Collectors.toList());
    }
    public List<UserAlertPreference> findAll(){ return new ArrayList<>(store.values()); }
}
