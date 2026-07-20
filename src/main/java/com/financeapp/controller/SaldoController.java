package com.financeapp.controller;

import com.financeapp.dto.SaldoResponse;
import com.financeapp.service.SaldoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saldo")
@RequiredArgsConstructor
public class SaldoController {

    private final SaldoService service;

    @GetMapping("/{ano}/{mes}")
    public SaldoResponse calcular(@PathVariable Integer ano, @PathVariable Integer mes) {
        return service.calcular(mes, ano);
    }
}
