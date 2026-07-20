package com.financeapp.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank String pergunta) {}
