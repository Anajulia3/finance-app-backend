package com.financeapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SenhaRequest(
    @NotBlank String senhaAtual,
    @NotBlank @Size(min = 6) String novaSenha
) {}
