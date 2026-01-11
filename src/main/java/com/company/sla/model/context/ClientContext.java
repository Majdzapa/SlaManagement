package com.company.sla.model.context;

import com.company.sla.annotation.SlaContext;
import lombok.Data; // Leaving it here but adding explicit getters just in case
import java.math.BigDecimal;

@Data
@SlaContext(displayName = "Client Transaction")
public class ClientContext {
    private String clientId;
    private String country;
    private String accountType; // PREMIUM, STANDARD
    private BigDecimal transactionAmount;
    private Integer riskScore;
    private Boolean isVIP;
    
    // Explicit Getters/Setters as Lombok fallback
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public BigDecimal getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(BigDecimal transactionAmount) { this.transactionAmount = transactionAmount; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public Boolean getIsVIP() { return isVIP; }
    public void setIsVIP(Boolean isVIP) { this.isVIP = isVIP; }
}
