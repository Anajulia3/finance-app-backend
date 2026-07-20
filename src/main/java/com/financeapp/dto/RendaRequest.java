package com.financeapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RendaRequest(
    @NotNull @Min(1) Integer mes,
    @NotNull @Min(2024) Integer ano,
    @NotNull @Positive BigDecimal valor
) {}
