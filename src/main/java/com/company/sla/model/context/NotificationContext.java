package com.company.sla.model.context;

import com.company.sla.annotation.SlaContext;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@SlaContext(displayName = "Notification Context")
public class NotificationContext {
    private String notificationType;
    private String notificationName;
    private String notificationNumber;

}
