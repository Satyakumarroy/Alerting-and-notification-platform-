package com.example.alerting.repository;

import com.example.alerting.model.Alert;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Repository
public class AlertRepository {
    private final Map<Long, Alert> store = new ConcurrentHashMap<>();
    public Alert save(Alert a){ store.put(a.getId(), a); return a; }
    public Optional<Alert> findById(long id){ return Optional.ofNullable(store.get(id)); }
    public List<Alert> findAll(){ return new ArrayList<>(store.values()); }
    public void delete(long id){ store.remove(id); }
    public List<Alert> filterActive(){
        Instant now = Instant.now();
        return store.values().stream()
                .filter(a -> (a.getStartAt() == null || !now.isBefore(a.getStartAt())) &&
                             (a.getExpiryAt() == null || !now.isAfter(a.getExpiryAt())))
                .collect(Collectors.toList());
    }
}
