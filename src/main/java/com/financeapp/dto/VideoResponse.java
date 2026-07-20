package com.financeapp.dto;

import com.financeapp.model.VideoConhecimento;

import java.time.LocalDateTime;

public record VideoResponse(Long id, String titulo, String url, boolean temTranscricao, LocalDateTime criadoEm) {

    public static VideoResponse of(VideoConhecimento v) {
        return new VideoResponse(
            v.getId(), v.getTitulo(), v.getUrl(),
            v.getTranscricao() != null && !v.getTranscricao().isBlank(),
            v.getCriadoEm()
        );
    }
}
