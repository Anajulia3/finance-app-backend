package com.financeapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_conhecimento")
@Data
@NoArgsConstructor
public class VideoConhecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String transcricao;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();
}
