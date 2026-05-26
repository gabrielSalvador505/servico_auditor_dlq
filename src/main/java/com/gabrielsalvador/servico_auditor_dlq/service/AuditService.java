package com.gabrielsalvador.servico_auditor_dlq.service;

import com.gabrielsalvador.servico_auditor_dlq.dto.OrderEventDto;
import com.gabrielsalvador.servico_auditor_dlq.dto.OrderItemDto;
import com.gabrielsalvador.servico_auditor_dlq.model.AuditLog;
import com.gabrielsalvador.servico_auditor_dlq.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void processarMensagemFalha(OrderEventDto evento, String jsonBruto) {
        AuditLog auditLog = new AuditLog();
        auditLog.setQueueName(evento.origin());
        auditLog.setPayload(jsonBruto);

        int totalProdutos = evento.orderItems().stream()
                .mapToInt(OrderItemDto::amount)
                .sum();

        String severidade = calcularSeveridade(totalProdutos);
        auditLog.setSeverity(severidade);

        repository.save(auditLog);
    }

    private String calcularSeveridade(int totalProdutos) {
        if (totalProdutos > 100) {
            return "HIGH";
        } else if (totalProdutos >= 50) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
}