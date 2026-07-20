package com.financeapp.repository;

import com.financeapp.model.RendaMensal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RendaMensalRepository extends JpaRepository<RendaMensal, Long> {
    Optional<RendaMensal> findByMesAndAnoAndUsuarioId(Integer mes, Integer ano, Long usuarioId);
}
