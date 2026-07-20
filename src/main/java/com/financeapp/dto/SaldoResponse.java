package com.financeapp.dto;

import java.math.BigDecimal;

public record SaldoResponse(
    Integer mes,
    Integer ano,
    BigDecimal renda,
    BigDecimal totalRealizado,
    BigDecimal totalPrevisto,
    BigDecimal saldoAtual,
    BigDecimal saldoProjetado
) {}
