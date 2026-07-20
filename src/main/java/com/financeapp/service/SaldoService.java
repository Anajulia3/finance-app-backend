package com.financeapp.service;

import com.financeapp.config.UsuarioLogado;
import com.financeapp.dto.SaldoResponse;
import com.financeapp.model.StatusGasto;
import com.financeapp.repository.GastoRepository;
import com.financeapp.repository.RendaMensalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaldoService {

    private final RendaMensalRepository rendaRepository;
    private final GastoRepository gastoRepository;
    private final UsuarioLogado usuarioLogado;

    public SaldoResponse calcular(Integer mes, Integer ano) {
        Long usuarioId = usuarioLogado.get().getId();

        BigDecimal renda = rendaRepository
                .findByMesAndAnoAndUsuarioId(mes, ano, usuarioId)
                .map(r -> r.getValor())
                .orElse(BigDecimal.ZERO);

        BigDecimal totalRealizado = gastoRepository.sumByMesAnoStatusAndUsuario(mes, ano, StatusGasto.REALIZADO, usuarioId);
        BigDecimal totalPrevisto = gastoRepository.sumByMesAnoStatusAndUsuario(mes, ano, StatusGasto.PREVISTO, usuarioId);

        return new SaldoResponse(mes, ano, renda, totalRealizado, totalPrevisto,
                renda.subtract(totalRealizado),
                renda.subtract(totalRealizado).subtract(totalPrevisto));
    }
}
