package com.financeapp.dto;

import com.financeapp.model.Categoria;
import com.financeapp.model.StatusGasto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoRequest(
    @NotBlank String descricao,
    @NotNull @Positive BigDecimal valor,
    @NotNull LocalDate data,
    @NotNull Categoria categoria,
    @NotNull StatusGasto status
) {}
