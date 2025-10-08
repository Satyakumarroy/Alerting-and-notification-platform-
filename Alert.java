package com.example.alerting.model;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Alert {
    private static final AtomicLong seq = new AtomicLong(1);
    private final long id;
    private String title;
    private String message;
    private Severity severity;
    private DeliveryType deliveryType;
    private Instant startAt;
    private Instant expiryAt;
    private boolean remindersEnabled = true;
    // Visibility: org-level, team ids, user ids
    private boolean orgWide = false;
    private Set<String> teamIds = new HashSet<>();
    private Set<String> userIds = new HashSet<>();
    private Instant createdAt;

    public Alert(){
        this.id = seq.getAndIncrement();
        this.createdAt = Instant.now();
    }
    // getters/setters...
    public long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public DeliveryType getDeliveryType() { return deliveryType; }
    public void setDeliveryType(DeliveryType deliveryType) { this.deliveryType = deliveryType; }
    public Instant getStartAt() { return startAt; }
    public void setStartAt(Instant startAt) { this.startAt = startAt; }
    public Instant getExpiryAt() { return expiryAt; }
    public void setExpiryAt(Instant expiryAt) { this.expiryAt = expiryAt; }
    public boolean isRemindersEnabled() { return remindersEnabled; }
    public void setRemindersEnabled(boolean remindersEnabled) { this.remindersEnabled = remindersEnabled; }
    public boolean isOrgWide() { return orgWide; }
    public void setOrgWide(boolean orgWide) { this.orgWide = orgWide; }
    public Set<String> getTeamIds() { return teamIds; }
    public Set<String> getUserIds() { return userIds; }
    public Instant getCreatedAt() { return createdAt; }
}
