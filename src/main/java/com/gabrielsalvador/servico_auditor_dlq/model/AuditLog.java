package com.gabrielsalvador.servico_auditor_dlq.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    private String errorId;
    private String queueName;
    
    @Column(columnDefinition = "TEXT")
    private String payload;
    
    private LocalDateTime timestamp;
    private String status;
    private String severity;

    public AuditLog() {
        this.errorId = UUID.randomUUID().toString();
        this.status = "PENDING_ANALYSIS";
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorId() { 
        return errorId; 
    }
    public String getQueueName() { 
        return queueName; 
    }
    public void setQueueName(String queueName) { 
        this.queueName = queueName;
    }
    public String getPayload() { 
        return payload;
    }
    public void setPayload(String payload) { 
        this.payload = payload;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) { 
        this.timestamp = timestamp;
    }
    public String getStatus() { 
        return status;
    }
    public String getSeverity() { 
        return severity;
    }
    public void setSeverity(String severity) { 
        this.severity = severity;
    }
}
