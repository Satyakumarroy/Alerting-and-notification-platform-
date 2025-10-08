package com.example.alerting.controller;

import com.example.alerting.model.Alert;
import com.example.alerting.model.Severity;
import com.example.alerting.repository.AlertRepository;
import com.example.alerting.service.AlertService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AlertService alertService;
    public AdminController(AlertService alertService){
        this.alertService = alertService;
    }

    @PostMapping("/alerts")
    public Alert createAlert(@RequestBody Alert alert){
        return alertService.createAlert(alert);
    }

    @PutMapping("/alerts/{id}")
    public Alert updateAlert(@PathVariable long id, @RequestBody Alert patch){
        return alertService.updateAlert(id, patch).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @GetMapping("/alerts")
    public List<Alert> listAlerts(@RequestParam Optional<Severity> severity, @RequestParam Optional<Boolean> active){
        return alertService.listFiltered(severity, active);
    }
}
