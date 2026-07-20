package com.financeapp.service;

import com.financeapp.config.UsuarioLogado;
import com.financeapp.dto.RecorrenteRequest;
import com.financeapp.model.*;
import com.financeapp.repository.GastoRepository;
import com.financeapp.repository.RecorrenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecorrenteService {

    private final RecorrenteRepository recorrenteRepository;
    private final GastoRepository gastoRepository;
    private final UsuarioLogado usuarioLogado;

    @Transactional
    public Recorrente criar(RecorrenteRequest req) {
        Usuario usuario = usuarioLogado.get();
        Recorrente r = new Recorrente();
        r.setDescricao(req.descricao());
        r.setValor(req.valor());
        r.setDia(req.dia());
        r.setCategoria(req.categoria());
        r.setDataInicio(req.dataInicio());
        r.setDataFim(req.dataFim());
        r.setAtivo(true);
        r.setUsuario(usuario);
        recorrenteRepository.save(r);

        gerarGastosMes(r, LocalDate.now());
        return r;
    }

    public List<Recorrente> listar() {
        return recorrenteRepository.findByUsuarioIdAndAtivoTrue(usuarioLogado.get().getId());
    }

    @Transactional
    public void desativar(Long id) {
        Long usuarioId = usuarioLogado.get().getId();
        Recorrente r = recorrenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!r.getUsuario().getId().equals(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        r.setAtivo(false);
        recorrenteRepository.save(r);
    }

    @Transactional
    public void gerarParaMes(Integer mes, Integer ano) {
        List<Recorrente> ativos = recorrenteRepository.findByUsuarioIdAndAtivoTrue(usuarioLogado.get().getId());
        LocalDate alvo = LocalDate.of(ano, mes, 1);
        for (Recorrente r : ativos) {
            gerarGastosMes(r, alvo);
        }
    }

    private void gerarGastosMes(Recorrente r, LocalDate mes) {
        int ultimoDia = YearMonth.of(mes.getYear(), mes.getMonthValue()).lengthOfMonth();
        int dia = Math.min(r.getDia(), ultimoDia);
        LocalDate dataGasto = LocalDate.of(mes.getYear(), mes.getMonthValue(), dia);

        if (dataGasto.isBefore(r.getDataInicio())) return;
        if (r.getDataFim() != null && dataGasto.isAfter(r.getDataFim())) return;

        Gasto gasto = new Gasto();
        gasto.setDescricao(r.getDescricao());
        gasto.setValor(r.getValor());
        gasto.setData(dataGasto);
        gasto.setCategoria(r.getCategoria());
        gasto.setStatus(StatusGasto.PREVISTO);
        gasto.setUsuario(r.getUsuario());
        gasto.setRecorrente(r);
        gastoRepository.save(gasto);
    }
}
