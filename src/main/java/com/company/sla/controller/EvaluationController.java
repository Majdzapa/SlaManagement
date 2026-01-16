package com.company.sla.controller;

import com.company.sla.dto.EvaluationRequest;
import com.company.sla.dto.EvaluationResponse;
import com.company.sla.service.SlaEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sla")
@CrossOrigin(origins = "http://localhost:4200")

public class EvaluationController {

    private final SlaEvaluationService evaluationService;

    public EvaluationController(SlaEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }




    @PostMapping("/{slaId}/evaluate")
    public ResponseEntity<EvaluationResponse> evaluate(@PathVariable Long slaId, @RequestBody EvaluationRequest request) {
        return ResponseEntity.ok(evaluationService.evaluate(slaId, request));
    }
}
