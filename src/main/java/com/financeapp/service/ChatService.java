package com.financeapp.service;

import com.financeapp.config.UsuarioLogado;
import com.financeapp.model.Gasto;
import com.financeapp.model.StatusGasto;
import com.financeapp.model.VideoConhecimento;
import com.financeapp.repository.GastoRepository;
import com.financeapp.repository.RendaMensalRepository;
import com.financeapp.repository.VideoConhecimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final GeminiService geminiService;
    private final VideoConhecimentoRepository videoRepo;
    private final GastoRepository gastoRepository;
    private final RendaMensalRepository rendaRepository;
    private final UsuarioLogado usuarioLogado;

    public String responder(String pergunta) {
        Long usuarioId = usuarioLogado.get().getId();
        LocalDate hoje = LocalDate.now();
        int mes = hoje.getMonthValue();
        int ano = hoje.getYear();

        BigDecimal renda = rendaRepository.findByMesAndAnoAndUsuarioId(mes, ano, usuarioId)
            .map(r -> r.getValor()).orElse(BigDecimal.ZERO);
        BigDecimal gastoRealizado = gastoRepository.sumByMesAnoStatusAndUsuario(mes, ano, StatusGasto.REALIZADO, usuarioId);
        BigDecimal gastoPrevisto  = gastoRepository.sumByMesAnoStatusAndUsuario(mes, ano, StatusGasto.PREVISTO,  usuarioId);
        List<Gasto> ultimos = gastoRepository.findByMesAnoAndUsuario(mes, ano, usuarioId).stream().limit(10).toList();
        List<VideoConhecimento> videos = videoRepo.findAll();

        StringBuilder prompt = new StringBuilder();
        prompt.append("""
            Você é o assistente financeiro do Saldo Vivo, um app de controle de finanças pessoais.
            Responda sempre em português, de forma clara e amigável.
            Use os dados financeiros do usuário e os conhecimentos abaixo para dar respostas personalizadas.
            Seja direto e objetivo. Quando relevante, dê sugestões práticas e motivadoras.

            """);

        prompt.append("=== DADOS DO USUÁRIO — ").append(mes).append("/").append(ano).append(" ===\n");
        prompt.append("Renda: R$ ").append(renda).append("\n");
        prompt.append("Gastos realizados: R$ ").append(gastoRealizado).append("\n");
        prompt.append("Gastos previstos: R$ ").append(gastoPrevisto).append("\n");
        prompt.append("Saldo disponível: R$ ").append(renda.subtract(gastoRealizado)).append("\n\n");

        if (!ultimos.isEmpty()) {
            prompt.append("Últimos gastos:\n");
            ultimos.forEach(g -> prompt.append("- ").append(g.getDescricao())
                .append(" (").append(g.getCategoria()).append("): R$ ").append(g.getValor()).append("\n"));
        }

        videos.stream()
            .filter(v -> v.getTranscricao() != null && !v.getTranscricao().isBlank())
            .forEach(v -> {
                prompt.append("\n=== EDUCAÇÃO FINANCEIRA: ").append(v.getTitulo()).append(" ===\n");
                String t = v.getTranscricao();
                prompt.append(t.length() > 6000 ? t.substring(0, 6000) + "..." : t).append("\n");
            });

        return geminiService.gerarResposta(prompt.toString(), pergunta);
    }
}
