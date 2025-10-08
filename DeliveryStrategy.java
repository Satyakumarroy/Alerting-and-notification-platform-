package com.example.alerting.delivery;

import com.example.alerting.model.Alert;
import com.example.alerting.model.User;

public interface DeliveryStrategy {
    void deliver(Alert alert, User user);
}
