package com.financeapp.dto;

import jakarta.validation.constraints.NotBlank;

public record VideoRequest(@NotBlank String url, String titulo) {}
