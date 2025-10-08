package com.example.alerting.repository;

import com.example.alerting.model.NotificationDelivery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class DeliveryRepository {
    private final List<NotificationDelivery> store = new CopyOnWriteArrayList<>();
    public void add(NotificationDelivery d){ store.add(d); }
    public List<NotificationDelivery> findAll(){ return new ArrayList<>(store); }
    public List<NotificationDelivery> findByAlertId(long alertId){
        return store.stream().filter(d -> d.getAlertId() == alertId).collect(Collectors.toList());
    }
    public List<NotificationDelivery> findByUserId(String userId){
        return store.stream().filter(d -> d.getUserId().equals(userId)).collect(Collectors.toList());
    }
}
