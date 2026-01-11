package com.company.sla.config;

import com.company.sla.model.result.SendNotificationResult;
import com.company.sla.repository.SendNotificationResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResultDataInitializer implements CommandLineRunner {

    private final SendNotificationResultRepository repository;

    public ResultDataInitializer(SendNotificationResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            SendNotificationResult r1 = new SendNotificationResult();
            r1.setMedia("EMAIL");
            r1.setBody("Please check the transaction.");
            r1.setRecipient("admin@company.com");
            repository.save(r1);

            SendNotificationResult r2 = new SendNotificationResult();
            r2.setMedia("SMS");
            r2.setBody("Urgent alert!");
            r2.setRecipient("+1234567890");
            repository.save(r2);
        }
    }
}
