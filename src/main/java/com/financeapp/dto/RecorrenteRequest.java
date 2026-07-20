package com.financeapp.dto;

import com.financeapp.model.Categoria;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecorrenteRequest(
    @NotBlank String descricao,
    @NotNull @Positive BigDecimal valor,
    @NotNull @Min(1) @Max(31) Integer dia,
    @NotNull Categoria categoria,
    @NotNull LocalDate dataInicio,
    LocalDate dataFim
) {}
