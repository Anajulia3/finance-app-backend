package com.financeapp.controller;

import com.financeapp.dto.RecorrenteRequest;
import com.financeapp.model.Recorrente;
import com.financeapp.service.RecorrenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recorrentes")
@RequiredArgsConstructor
public class RecorrenteController {

    private final RecorrenteService service;

    @PostMapping
    public ResponseEntity<Recorrente> criar(@Valid @RequestBody RecorrenteRequest req) {
        return ResponseEntity.ok(service.criar(req));
    }

    @GetMapping
    public List<Recorrente> listar() {
        return service.listar();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/gerar/{ano}/{mes}")
    public ResponseEntity<Void> gerar(@PathVariable Integer ano, @PathVariable Integer mes) {
        service.gerarParaMes(mes, ano);
        return ResponseEntity.ok().build();
    }
}
