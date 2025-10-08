package com.example.alerting.delivery;

import com.example.alerting.model.Alert;
import com.example.alerting.model.NotificationDelivery;
import com.example.alerting.model.User;
import com.example.alerting.repository.DeliveryRepository;
import com.example.alerting.repository.UserAlertPrefRepository;
import org.springframework.stereotype.Component;

@Component("IN_APP")
public class InAppDeliveryStrategy implements DeliveryStrategy {
    private final DeliveryRepository deliveryRepository;
    private final UserAlertPrefRepository prefRepository;

    public InAppDeliveryStrategy(DeliveryRepository dr, UserAlertPrefRepository prefRepository){
        this.deliveryRepository = dr;
        this.prefRepository = prefRepository;
    }

    @Override
    public void deliver(Alert alert, User user) {
        // create delivery log
        deliveryRepository.add(new NotificationDelivery(alert.getId(), user.getId()));
        // ensure preference exists (unread by default)
        prefRepository.getOrCreate(alert.getId(), user.getId());
        // For real in-app: push via websockets / push service; here we log delivery.
        System.out.printf("Delivered alert %d to user %s (in-app)\n", alert.getId(), user.getName());
    }
}
