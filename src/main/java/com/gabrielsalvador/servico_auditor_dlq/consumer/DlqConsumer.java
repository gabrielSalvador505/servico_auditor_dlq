package com.gabrielsalvador.servico_auditor_dlq.consumer;

import com.gabrielsalvador.servico_auditor_dlq.dto.OrderEventDto;
import com.gabrielsalvador.servico_auditor_dlq.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class DlqConsumer {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public DlqConsumer(AuditService auditService, ObjectMapper objectMapper) {
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${queue.order-events}")
    public void escutarDlq(String mensagemBruta) {
        try {
            System.out.println("Mensagem recebida da DLQ: " + mensagemBruta);

            OrderEventDto evento = objectMapper.readValue(mensagemBruta, OrderEventDto.class);

            auditService.processarMensagemFalha(evento, mensagemBruta);
            
            System.out.println("Mensagem processada e salva no banco com sucesso!");

        } catch (JsonProcessingException e) {
            System.err.println("Erro ao desserializar payload: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro crítico no processamento da DLQ: " + e.getMessage());
            throw e; 
        }
    }
}