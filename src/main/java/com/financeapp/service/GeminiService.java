package com.financeapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String gerarResposta(String systemPrompt, String pergunta) {
        Map<String, Object> body = Map.of(
            "system_instruction", Map.of("parts", List.of(Map.of("text", systemPrompt))),
            "contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", pergunta))))
        );
        try {
            Map<?, ?> response = restTemplate.postForObject(URL + apiKey, body, Map.class);
            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
            List<?> parts = (List<?>) content.get("parts");
            return (String) ((Map<?, ?>) parts.get(0)).get("text");
        } catch (Exception e) {
            log.error("Erro ao chamar Gemini: {}", e.getMessage());
            throw new RuntimeException("Falha ao gerar resposta da IA");
        }
    }
}
