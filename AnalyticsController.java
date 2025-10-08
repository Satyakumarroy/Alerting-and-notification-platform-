package com.example.alerting.controller;

import com.example.alerting.model.Alert;
import com.example.alerting.model.NotificationDelivery;
import com.example.alerting.model.Severity;
import com.example.alerting.repository.AlertRepository;
import com.example.alerting.repository.DeliveryRepository;
import com.example.alerting.repository.UserAlertPrefRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AlertRepository alertRepo;
    private final DeliveryRepository deliveryRepo;
    private final UserAlertPrefRepository prefRepo;

    public AnalyticsController(AlertRepository alertRepo, DeliveryRepository deliveryRepo, UserAlertPrefRepository prefRepo){
        this.alertRepo = alertRepo; this.deliveryRepo = deliveryRepo; this.prefRepo = prefRepo;
    }

    @GetMapping
    public Map<String,Object> analytics(){
        Map<String,Object> out = new HashMap<>();
        List<Alert> alerts = alertRepo.findAll();
        out.put("totalAlerts", alerts.size());
        List<NotificationDelivery> deliveries = deliveryRepo.findAll();
        out.put("deliveredCount", deliveries.size());
        long read = prefRepo.findAll().stream().filter(p -> p.getState() == com.example.alerting.model.UserAlertPreference.State.READ).count();
        out.put("readCount", read);
        Map<Severity, Long> breakdown = alerts.stream().collect(Collectors.groupingBy(Alert::getSeverity, Collectors.counting()));
        out.put("bySeverity", breakdown);
        // snoozed counts per alert
        Map<Long, Long> snoozed = prefRepo.findAll().stream()
                .filter(p -> p.getState() == com.example.alerting.model.UserAlertPreference.State.SNOOZED)
                .collect(Collectors.groupingBy(com.example.alerting.model.UserAlertPreference::getAlertId, Collectors.counting()));
        out.put("snoozedCountsPerAlert", snoozed);
        return out;
    }
}
