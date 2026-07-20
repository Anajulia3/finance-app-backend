package com.financeapp.controller;

import com.financeapp.dto.GastoRequest;
import com.financeapp.model.Gasto;
import com.financeapp.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService service;

    @PostMapping
    public ResponseEntity<Gasto> criar(@Valid @RequestBody GastoRequest request) {
        return ResponseEntity.ok(service.salvar(request));
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<List<Gasto>> listar(@PathVariable Integer ano, @PathVariable Integer mes) {
        return ResponseEntity.ok(service.listarPorMes(mes, ano));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
