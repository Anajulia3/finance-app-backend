package com.financeapp.repository;

import com.financeapp.model.VideoConhecimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoConhecimentoRepository extends JpaRepository<VideoConhecimento, Long> {
    boolean existsByUrl(String url);
}
