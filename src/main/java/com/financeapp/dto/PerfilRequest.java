package com.financeapp.dto;

import jakarta.validation.constraints.NotBlank;

public record PerfilRequest(@NotBlank String nome) {}
