package com.example.alerting.model;

import java.time.LocalDate;
import java.time.Instant;

public class UserAlertPreference {
    public enum State { UNREAD, READ, SNOOZED }

    private final String id = java.util.UUID.randomUUID().toString();
    private final long alertId;
    private final String userId;
    private State state;
    private Instant lastUpdated;
    // snooze date: date for which snooze is active (local date)
    private LocalDate snoozedForDate;

    public UserAlertPreference(long alertId, String userId){
        this.alertId = alertId; this.userId = userId; this.state = State.UNREAD; this.lastUpdated = Instant.now();
    }

    public String getId() { return id; }
    public long getAlertId() { return alertId; }
    public String getUserId() { return userId; }
    public State getState() { return state; }
    public LocalDate getSnoozedForDate() { return snoozedForDate; }
    public void setState(State state){ this.state = state; this.lastUpdated = Instant.now(); }
    public void snoozeForDate(LocalDate date){ this.snoozedForDate = date; this.state = State.SNOOZED; this.lastUpdated = Instant.now(); }
    public void clearSnooze(){ this.snoozedForDate = null; if (this.state==State.SNOOZED) this.state = State.UNREAD; this.lastUpdated = Instant.now(); }
}
