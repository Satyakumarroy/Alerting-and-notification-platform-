package com.example.alerting.service;

import com.example.alerting.model.*;
import com.example.alerting.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final AlertRepository alertRepo;
    private final UserAlertPrefRepository prefRepo;

    public UserService(UserRepository userRepo, AlertRepository alertRepo, UserAlertPrefRepository prefRepo){
        this.userRepo = userRepo; this.alertRepo = alertRepo; this.prefRepo = prefRepo;
    }

    public List<Alert> getActiveAlertsForUser(String userId){
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) return List.of();
        User user = userOpt.get();
        // resolve active alerts that apply to this user
        return alertRepo.findAll().stream()
                .filter(a -> {
                    // active
                    var now = java.time.Instant.now();
                    boolean active = (a.getStartAt()==null || !now.isBefore(a.getStartAt())) &&
                                     (a.getExpiryAt()==null || !now.isAfter(a.getExpiryAt()));
                    if (!active) return false;
                    // visibility
                    if (a.isOrgWide()) return true;
                    if (a.getUserIds().contains(userId)) return true;
                    if (a.getTeamIds().contains(user.getTeamId())) return true;
                    return false;
                }).collect(Collectors.toList());
    }

    public boolean snoozeAlertForDay(String userId, long alertId){
        UserAlertPreference pref = prefRepo.getOrCreate(alertId, userId);
        pref.snoozeForDate(LocalDate.now());
        return true;
    }

    public boolean markReadOrUnread(String userId, long alertId, boolean read){
        UserAlertPreference pref = prefRepo.getOrCreate(alertId, userId);
        pref.clearSnooze(); // clearing snooze when acking? keep simple: reading clears snooze
        if (read) pref.setState(UserAlertPreference.State.READ);
        else pref.setState(UserAlertPreference.State.UNREAD);
        return true;
    }
}
