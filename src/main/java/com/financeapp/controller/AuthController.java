package com.financeapp.controller;

import com.financeapp.config.JwtUtil;
import com.financeapp.config.UsuarioLogado;
import com.financeapp.dto.AuthResponse;
import com.financeapp.dto.LoginRequest;
import com.financeapp.dto.PerfilRequest;
import com.financeapp.dto.RegisterRequest;
import com.financeapp.dto.SenhaRequest;
import com.financeapp.model.Usuario;
import com.financeapp.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final UsuarioLogado usuarioLogado;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(req.nome());
        usuario.setEmail(req.email());
        usuario.setSenha(passwordEncoder.encode(req.senha()));
        usuarioRepository.save(usuario);

        String token = jwtUtil.gerar(usuario.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, usuario.getNome(), usuario.getEmail()));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.senha()));
        Usuario usuario = usuarioRepository.findByEmail(req.email()).orElseThrow();
        String token = jwtUtil.gerar(usuario.getEmail());
        return new AuthResponse(token, usuario.getNome(), usuario.getEmail());
    }

    @PutMapping("/perfil")
    public AuthResponse atualizarPerfil(@Valid @RequestBody PerfilRequest req) {
        Usuario usuario = usuarioLogado.get();
        usuario.setNome(req.nome());
        usuarioRepository.save(usuario);
        String token = jwtUtil.gerar(usuario.getEmail());
        return new AuthResponse(token, usuario.getNome(), usuario.getEmail());
    }

    @PutMapping("/senha")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody SenhaRequest req) {
        Usuario usuario = usuarioLogado.get();
        if (!passwordEncoder.matches(req.senhaAtual(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }
        usuario.setSenha(passwordEncoder.encode(req.novaSenha()));
        usuarioRepository.save(usuario);
        return ResponseEntity.noContent().build();
    }
}
