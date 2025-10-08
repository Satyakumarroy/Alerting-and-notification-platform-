package com.example.alerting.controller;

import com.example.alerting.model.Alert;
import com.example.alerting.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){ this.userService = userService; }

    @GetMapping("/{userId}/alerts")
    public List<Alert> getAlerts(@PathVariable String userId){
        return userService.getActiveAlertsForUser(userId);
    }

    @PostMapping("/{userId}/alerts/{alertId}/snooze")
    public String snooze(@PathVariable String userId, @PathVariable long alertId){
        userService.snoozeAlertForDay(userId, alertId);
        return "Snoozed for today";
    }

    @PostMapping("/{userId}/alerts/{alertId}/mark")
    public String mark(@PathVariable String userId, @PathVariable long alertId, @RequestParam boolean read){
        userService.markReadOrUnread(userId, alertId, read);
        return read ? "Marked read" : "Marked unread";
    }
}
