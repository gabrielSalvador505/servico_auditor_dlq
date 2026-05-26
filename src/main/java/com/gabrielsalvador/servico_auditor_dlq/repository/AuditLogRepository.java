package com.gabrielsalvador.servico_auditor_dlq.repository;

import com.gabrielsalvador.servico_auditor_dlq.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    
}