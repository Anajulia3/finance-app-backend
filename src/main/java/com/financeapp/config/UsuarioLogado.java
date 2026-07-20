package com.financeapp.config;

import com.financeapp.model.Usuario;
import com.financeapp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioLogado {

    private final UsuarioRepository usuarioRepository;

    public Usuario get() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email).orElseThrow();
    }
}
