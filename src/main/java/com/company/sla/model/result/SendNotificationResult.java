package com.company.sla.model.result;

import com.company.sla.annotation.SlaResult;
import jakarta.persistence.*;

@Entity
@SlaResult(displayName = "Send Notification")
public class SendNotificationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String media; // e.g. EMAIL, SMS
    private String body;
    private String recipient;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedia() { return media; }
    public void setMedia(String media) { this.media = media; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
}
