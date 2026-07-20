package com.financeapp.service;

import com.financeapp.config.UsuarioLogado;
import com.financeapp.dto.GastoRequest;
import com.financeapp.model.Gasto;
import com.financeapp.model.Usuario;
import com.financeapp.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository repository;
    private final UsuarioLogado usuarioLogado;

    @Transactional
    public Gasto salvar(GastoRequest request) {
        Usuario usuario = usuarioLogado.get();
        Gasto gasto = new Gasto();
        gasto.setDescricao(request.descricao());
        gasto.setValor(request.valor());
        gasto.setData(request.data());
        gasto.setCategoria(request.categoria());
        gasto.setStatus(request.status());
        gasto.setUsuario(usuario);
        return repository.save(gasto);
    }

    public List<Gasto> listarPorMes(Integer mes, Integer ano) {
        return repository.findByMesAnoAndUsuario(mes, ano, usuarioLogado.get().getId());
    }

    @Transactional
    public void deletar(Long id) {
        Long usuarioId = usuarioLogado.get().getId();
        Gasto gasto = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!gasto.getUsuario().getId().equals(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        repository.deleteById(id);
    }
}
