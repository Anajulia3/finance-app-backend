package com.financeapp.repository;

import com.financeapp.model.Recorrente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecorrenteRepository extends JpaRepository<Recorrente, Long> {
    List<Recorrente> findByUsuarioIdAndAtivoTrue(Long usuarioId);
}
