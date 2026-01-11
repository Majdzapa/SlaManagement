package com.company.sla.dto;

import java.util.Map;

public class EvaluationRequest {
    private Map<String, Object> context;

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
}
