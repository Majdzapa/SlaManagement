package com.company.sla.controller;

import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaResultMapping;
import com.company.sla.model.SlaRule;
import com.company.sla.repository.SlaResultMappingRepository;
import com.company.sla.repository.SlaRuleRepository;
import com.company.sla.service.SlaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sla")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class SlaController {

    private final SlaService slaService;
    private final SlaRuleRepository ruleRepository;
    private final SlaResultMappingRepository mappingRepository;
    private final com.company.sla.service.SlaContextService slaContextService;

    // --- Contexts ---
    @GetMapping("/contexts")
    public ResponseEntity<List<com.company.sla.dto.ContextDto.ContextClassInfo>> getAvailableContexts() {
        return ResponseEntity.ok(slaContextService.getAvailableContexts());
    }

    @GetMapping("/result-types")
    public ResponseEntity<List<com.company.sla.dto.ContextDto.ContextClassInfo>> getAvailableResultTypes() {
        return ResponseEntity.ok(slaContextService.getAvailableResultTypes());
    }

    @GetMapping("/result-instances/{className}")
    public ResponseEntity<List<Object>> getResultInstances(@PathVariable String className) {
        return ResponseEntity.ok(slaContextService.getResultInstances(className));
    }

    // --- Configuration ---
    @PostMapping("/configuration")
    public ResponseEntity<SlaConfiguration> createSla(@RequestBody SlaConfiguration sla) {
        return ResponseEntity.ok(slaService.createSla(sla));
    }

    @PutMapping("/configuration/{id}")
    public ResponseEntity<SlaConfiguration> updateSla(@PathVariable Long id, @RequestBody SlaConfiguration sla) {
        return ResponseEntity.ok(slaService.updateSla(id, sla));
    }

    @GetMapping("/configuration")
    public ResponseEntity<List<SlaConfiguration>> getAllSlas() {
        return ResponseEntity.ok(slaService.getAllSlas());
    }

    @GetMapping("/configuration/{id}")
    public ResponseEntity<SlaConfiguration> getSlaById(@PathVariable Long id) {
        return slaService.getSlaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/configuration/{id}")
    public ResponseEntity<Void> deleteSla(@PathVariable Long id) {
        slaService.deleteSla(id);
        return ResponseEntity.noContent().build();
    }

    // --- Rules ---
    @GetMapping("/{slaId}/rules")
    public ResponseEntity<List<SlaRule>> getRules(@PathVariable Long slaId) {
        return ResponseEntity.ok(ruleRepository.findBySlaConfigurationIdOrderByRuleOrderAsc(slaId));
    }

    @PostMapping("/{slaId}/rules")
    public ResponseEntity<SlaRule> addRule(@PathVariable Long slaId, @RequestBody SlaRule rule) {
        return slaService.getSlaById(slaId).map(sla -> {
            rule.setSlaConfiguration(sla);
            return ResponseEntity.ok(ruleRepository.save(rule));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/rules/{ruleId}")
    public ResponseEntity<SlaRule> updateRule(@PathVariable Long ruleId, @RequestBody SlaRule ruleDetails) {
        return ruleRepository.findById(ruleId).map(rule -> {
            rule.setRuleName(ruleDetails.getRuleName());
            rule.setConditionField(ruleDetails.getConditionField());
            rule.setConditionOperator(ruleDetails.getConditionOperator());
            rule.setConditionValue(ruleDetails.getConditionValue());
            rule.setWeight(ruleDetails.getWeight());
            rule.setRuleOrder(ruleDetails.getRuleOrder());
            return ResponseEntity.ok(ruleRepository.save(rule));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/rules/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long ruleId) {
        ruleRepository.deleteById(ruleId);
        return ResponseEntity.noContent().build();
    }

    // --- Results ---
    @GetMapping("/{slaId}/results")
    public ResponseEntity<List<SlaResultMapping>> getResults(@PathVariable Long slaId) {
        return ResponseEntity.ok(mappingRepository.findBySlaConfigurationId(slaId));
    }

    @PostMapping("/{slaId}/results")
    public ResponseEntity<SlaResultMapping> addResult(@PathVariable Long slaId, @RequestBody SlaResultMapping mapping) {
        return slaService.getSlaById(slaId).map(sla -> {
            mapping.setSlaConfiguration(sla);
            return ResponseEntity.ok(mappingRepository.save(mapping));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/results/{mappingId}")
    public ResponseEntity<SlaResultMapping> updateResult(@PathVariable Long mappingId, @RequestBody SlaResultMapping details) {
        return mappingRepository.findById(mappingId).map(mapping -> {
            mapping.setMinWeight(details.getMinWeight());
            mapping.setMaxWeight(details.getMaxWeight());
            mapping.setResultValue(details.getResultValue());
            mapping.setDescription(details.getDescription());
            return ResponseEntity.ok(mappingRepository.save(mapping));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/results/{mappingId}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long mappingId) {
        mappingRepository.deleteById(mappingId);
        return ResponseEntity.noContent().build();
    }
}
