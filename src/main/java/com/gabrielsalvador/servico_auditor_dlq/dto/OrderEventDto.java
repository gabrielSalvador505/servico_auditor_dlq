package com.gabrielsalvador.servico_auditor_dlq.dto;

import java.util.List;

public record OrderEventDto(
    String zipCode,
    Long customerId,
    List<OrderItemDto> orderItems,
    String origin,
    String occurredAt
) {}
