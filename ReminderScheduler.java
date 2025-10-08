package com.example.alerting.scheduler;

import com.example.alerting.model.Alert;
import com.example.alerting.service.AlertService;
import com.example.alerting.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ReminderScheduler {
    private final AlertRepository alertRepository;
    private final AlertService alertService;

    public ReminderScheduler(AlertRepository alertRepository, AlertService alertService){
        this.alertRepository = alertRepository; this.alertService = alertService;
    }

    @Value("${app.reminderIntervalMinutes:120}")
    private long reminderIntervalMinutes;

    // Runs every reminderIntervalMinutes. For demo you can lower this in application.yml.
    @Scheduled(fixedRateString = "${app.reminderIntervalMinutes:120}000", initialDelay = 5000)
    public void triggerReminders(){
        // WARNING: fixedRateString is in ms; we multiply in property. To keep simple, property set in minutes appended with 000
        System.out.println("ReminderScheduler: triggering reminders");
        List<Alert> active = alertRepository.findAll();
        for (Alert a : active){
            alertService.sendAlertToAudience(a);
        }
    }
}
