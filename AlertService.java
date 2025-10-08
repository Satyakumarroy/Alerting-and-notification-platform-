package com.example.alerting.service;

import com.example.alerting.model.*;
import com.example.alerting.repository.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlertService {
    private final AlertRepository alertRepo;
    private final UserRepository userRepo;
    private final TeamRepository teamRepo;
    private final DeliveryService deliveryService;
    private final UserAlertPrefRepository prefRepo;

    public AlertService(AlertRepository alertRepo, UserRepository userRepo, TeamRepository teamRepo,
                        DeliveryService deliveryService, UserAlertPrefRepository prefRepo){
        this.alertRepo = alertRepo;
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
        this.deliveryService = deliveryService;
        this.prefRepo = prefRepo;
    }

    public Alert createAlert(Alert a){
        // default start now if null
        if (a.getStartAt() == null) a.setStartAt(Instant.now());
        alertRepo.save(a);
        return a;
    }

    public Optional<Alert> updateAlert(long id, Alert patch){
        Optional<Alert> ex = alertRepo.findById(id);
        ex.ifPresent(a -> {
            if (patch.getTitle() != null) a.setTitle(patch.getTitle());
            if (patch.getMessage() != null) a.setMessage(patch.getMessage());
            if (patch.getSeverity() != null) a.setSeverity(patch.getSeverity());
            if (patch.getDeliveryType() != null) a.setDeliveryType(patch.getDeliveryType());
            if (patch.getStartAt() != null) a.setStartAt(patch.getStartAt());
            if (patch.getExpiryAt() != null) a.setExpiryAt(patch.getExpiryAt());
            a.setRemindersEnabled(patch.isRemindersEnabled());
            // copy visibility if provided
            if (patch.isOrgWide()) a.setOrgWide(true);
            if (!patch.getTeamIds().isEmpty()) a.getTeamIds().clear(); a.getTeamIds().addAll(patch.getTeamIds());
            if (!patch.getUserIds().isEmpty()) a.getUserIds().clear(); a.getUserIds().addAll(patch.getUserIds());
            alertRepo.save(a);
        });
        return ex;
    }

    public List<Alert> listAll(){ return alertRepo.findAll(); }

    public List<Alert> listFiltered(Optional<Severity> severity, Optional<Boolean> active){
        List<Alert> all = alertRepo.findAll();
        return all.stream().filter(a -> {
            if (severity.isPresent() && a.getSeverity() != severity.get()) return false;
            if (active.isPresent()){
                Instant now = Instant.now();
                boolean isActive = (a.getStartAt()==null || !now.isBefore(a.getStartAt())) &&
                                   (a.getExpiryAt()==null || !now.isAfter(a.getExpiryAt()));
                if (!active.get().equals(isActive)) return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    // Determine audience users for an alert
    public List<User> resolveAudience(Alert a){
        if (a.isOrgWide()) return userRepo.findAll();
        Set<User> users = new HashSet<>();
        for (String teamId : a.getTeamIds()){
            users.addAll(userRepo.findByTeamId(teamId));
        }
        for (String userId : a.getUserIds()){
            userRepo.findById(userId).ifPresent(users::add);
        }
        return new ArrayList<>(users);
    }

    // send alert once to resolved users (used by scheduler)
    public void sendAlertToAudience(Alert a){
        if (!a.isRemindersEnabled()) return;
        if (a.getStartAt()!=null && Instant.now().isBefore(a.getStartAt())) return;
        if (a.getExpiryAt()!=null && Instant.now().isAfter(a.getExpiryAt())) return;
        List<User> audience = resolveAudience(a);
        for (User user : audience){
            // skip if user snoozed today
            UserAlertPreference pref = prefRepo.getOrCreate(a.getId(), user.getId());
            if (pref.getSnoozedForDate() != null && pref.getSnoozedForDate().equals(LocalDate.now())) {
                // user snoozed for today
                continue;
            }
            deliveryService.deliverToUser(a, user);
        }
    }
}
