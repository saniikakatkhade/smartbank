package com.smartbook.controller;

import com.smartbook.fraud.FraudAlert;
import com.smartbook.repository.FraudAlertRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud-alerts")
public class FraudAlertController {

    private final FraudAlertRepository fraudAlertRepository;

    public FraudAlertController(
            FraudAlertRepository fraudAlertRepository) {
        this.fraudAlertRepository = fraudAlertRepository;
    }

    @GetMapping
    public ResponseEntity<List<FraudAlert>> getAllFraudAlerts() {

        return ResponseEntity.ok(
                fraudAlertRepository.findAll()
        );
    }
}