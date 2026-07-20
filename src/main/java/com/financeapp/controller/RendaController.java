package com.financeapp.controller;

import com.financeapp.dto.RendaRequest;
import com.financeapp.model.RendaMensal;
import com.financeapp.service.RendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/renda")
@RequiredArgsConstructor
public class RendaController {

    private final RendaService service;

    @PostMapping
    public ResponseEntity<RendaMensal> salvar(@Valid @RequestBody RendaRequest request) {
        return ResponseEntity.ok(service.salvar(request));
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<RendaMensal> buscar(@PathVariable Integer ano, @PathVariable Integer mes) {
        return service.buscar(mes, ano)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
