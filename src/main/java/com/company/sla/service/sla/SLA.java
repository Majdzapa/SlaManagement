package com.company.sla.service.sla;

import com.company.sla.model.SlaRule;
import java.util.List;

public interface SLA<T, R> {
    R evaluate(T context);
    String getSlaType();
    List<SlaRule> getRules();
}
