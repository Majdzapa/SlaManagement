package com.company.sla.service;

import com.company.sla.model.SlaConfiguration;
import com.company.sla.repository.SlaConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SlaService {

    private final SlaConfigurationRepository slaConfigurationRepository;

    public List<SlaConfiguration> getAllSlas() {
        return slaConfigurationRepository.findAll();
    }

    public Optional<SlaConfiguration> getSlaById(Long id) {
        return slaConfigurationRepository.findById(id);
    }

    @Transactional
    public SlaConfiguration createSla(SlaConfiguration slaConfiguration) {
        if (slaConfigurationRepository.existsBySlaName(slaConfiguration.getSlaName())) {
            throw new IllegalArgumentException("SLA with name " + slaConfiguration.getSlaName() + " already exists");
        }
        return slaConfigurationRepository.save(slaConfiguration);
    }

    @Transactional
    public SlaConfiguration updateSla(Long id, SlaConfiguration slaDetails) {
        SlaConfiguration sla = slaConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA not found for this id :: " + id));

        sla.setSlaName(slaDetails.getSlaName());
        sla.setSlaType(slaDetails.getSlaType());
        sla.setContextType(slaDetails.getContextType());
        sla.setResultType(slaDetails.getResultType());
        sla.setActive(slaDetails.isActive());
        
        return slaConfigurationRepository.save(sla);
    }

    @Transactional
    public void deleteSla(Long id) {
        SlaConfiguration sla = slaConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA not found for this id :: " + id));
        slaConfigurationRepository.delete(sla);
    }
}
