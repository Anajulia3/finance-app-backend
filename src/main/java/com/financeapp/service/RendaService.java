package com.financeapp.service;

import com.financeapp.config.UsuarioLogado;
import com.financeapp.dto.RendaRequest;
import com.financeapp.model.RendaMensal;
import com.financeapp.model.Usuario;
import com.financeapp.repository.RendaMensalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RendaService {

    private final RendaMensalRepository repository;
    private final UsuarioLogado usuarioLogado;

    @Transactional
    public RendaMensal salvar(RendaRequest request) {
        Usuario usuario = usuarioLogado.get();
        RendaMensal renda = repository
                .findByMesAndAnoAndUsuarioId(request.mes(), request.ano(), usuario.getId())
                .orElse(new RendaMensal());
        renda.setMes(request.mes());
        renda.setAno(request.ano());
        renda.setValor(request.valor());
        renda.setUsuario(usuario);
        return repository.save(renda);
    }

    public Optional<RendaMensal> buscar(Integer mes, Integer ano) {
        return repository.findByMesAndAnoAndUsuarioId(mes, ano, usuarioLogado.get().getId());
    }
}
