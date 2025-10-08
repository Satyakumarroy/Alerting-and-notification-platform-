package com.example.alerting.model;

import java.time.Instant;
import java.util.UUID;

public class NotificationDelivery {
    private final String id = UUID.randomUUID().toString();
    private final long alertId;
    private final String userId;
    private final Instant deliveredAt;
    public NotificationDelivery(long alertId, String userId) {
        this.alertId = alertId; this.userId = userId; this.deliveredAt = Instant.now();
    }
    public String getId() { return id; }
    public long getAlertId() { return alertId; }
    public String getUserId() { return userId; }
    public Instant getDeliveredAt() { return deliveredAt; }
}
