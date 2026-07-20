package com.financeapp.repository;

import com.financeapp.model.Gasto;
import com.financeapp.model.StatusGasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @Query("SELECT g FROM Gasto g WHERE MONTH(g.data) = :mes AND YEAR(g.data) = :ano AND g.usuario.id = :usuarioId ORDER BY g.data DESC")
    List<Gasto> findByMesAnoAndUsuario(Integer mes, Integer ano, Long usuarioId);

    @Query("SELECT COALESCE(SUM(g.valor), 0) FROM Gasto g WHERE MONTH(g.data) = :mes AND YEAR(g.data) = :ano AND g.status = :status AND g.usuario.id = :usuarioId")
    BigDecimal sumByMesAnoStatusAndUsuario(Integer mes, Integer ano, StatusGasto status, Long usuarioId);
}
