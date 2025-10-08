package com.example.alerting.config;

import com.example.alerting.model.*;
import com.example.alerting.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DataSeeder implements ApplicationRunner {
    private final TeamRepository teamRepo;
    private final UserRepository userRepo;
    private final AlertRepository alertRepo;

    public DataSeeder(TeamRepository teamRepo, UserRepository userRepo, AlertRepository alertRepo){
        this.teamRepo = teamRepo; this.userRepo = userRepo; this.alertRepo = alertRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // seed teams
        Team eng = new Team("Engineering");
        Team mkt = new Team("Marketing");
        Team ops = new Team("Operations");
        teamRepo.save(eng); teamRepo.save(mkt); teamRepo.save(ops);
        // seed users
        User u1 = new User("Alice", eng.getId(), "alice@example.com");
        User u2 = new User("Bob", eng.getId(), "bob@example.com");
        User u3 = new User("Charlie", mkt.getId(), "charlie@example.com");
        User u4 = new User("Dana", ops.getId(), "dana@example.com");
        User u5 = new User("Eve", ops.getId(), "eve@example.com");
        userRepo.save(u1); userRepo.save(u2); userRepo.save(u3); userRepo.save(u4); userRepo.save(u5);

        // seed alerts
        Alert a1 = new Alert();
        a1.setTitle("Scheduled maintenance");
        a1.setMessage("DB maintenance at midnight UTC");
        a1.setSeverity(Severity.WARNING);
        a1.setDeliveryType(DeliveryType.IN_APP);
        a1.setOrgWide(false);
        a1.getTeamIds().add(eng.getId());
        a1.setStartAt(Instant.now().minusSeconds(60));
        a1.setExpiryAt(Instant.now().plusSeconds(3600));
        alertRepo.save(a1);

        Alert a2 = new Alert();
        a2.setTitle("Policy update");
        a2.setMessage("Updated privacy policy - please review");
        a2.setSeverity(Severity.INFO);
        a2.setDeliveryType(DeliveryType.IN_APP);
        a2.setOrgWide(true);
        a2.setStartAt(Instant.now().minusSeconds(60));
        a2.setExpiryAt(Instant.now().plusSeconds(86400));
        alertRepo.save(a2);
        System.out.println("Seeded sample teams, users, alerts");
    }
}
