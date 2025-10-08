package com.example.alerting.service;

import com.example.alerting.delivery.DeliveryStrategy;
import com.example.alerting.model.Alert;
import com.example.alerting.model.User;
import com.example.alerting.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    private final ApplicationContext ctx;
    private final DeliveryRepository deliveryRepository;
    public DeliveryService(ApplicationContext ctx, DeliveryRepository deliveryRepository){
        this.ctx = ctx; this.deliveryRepository = deliveryRepository;
    }

    public void deliverToUser(Alert alert, User user){
        String beanName = alert.getDeliveryType() == null ? "IN_APP" : alert.getDeliveryType().name();
        DeliveryStrategy strategy = (DeliveryStrategy) ctx.getBean(beanName);
        strategy.deliver(alert, user);
    }
}
