package com.financeapp.service;

import com.financeapp.dto.VideoRequest;
import com.financeapp.dto.VideoResponse;
import com.financeapp.model.VideoConhecimento;
import com.financeapp.repository.VideoConhecimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoConhecimentoRepository videoRepo;
    private final YoutubeService youtubeService;

    public List<VideoResponse> listar() {
        return videoRepo.findAll().stream().map(VideoResponse::of).toList();
    }

    public VideoResponse adicionar(VideoRequest request) {
        if (videoRepo.existsByUrl(request.url())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vídeo já cadastrado");
        }
        VideoConhecimento video = new VideoConhecimento();
        video.setUrl(request.url());

        String videoId = youtubeService.extrairVideoId(request.url());
        if (request.titulo() != null && !request.titulo().isBlank()) {
            video.setTitulo(request.titulo());
        } else {
            video.setTitulo(videoId != null ? youtubeService.buscarTitulo(videoId) : request.url());
        }

        if (videoId != null) {
            video.setTranscricao(youtubeService.buscarTranscricao(request.url()));
        }

        return VideoResponse.of(videoRepo.save(video));
    }

    public Map<String, Integer> importarCanal(String channelUrl) {
        List<String> videoIds = youtubeService.buscarVideosDoCanal(channelUrl);

        int importados = 0, comTranscricao = 0, ignorados = 0;

        for (String videoId : videoIds) {
            String url = "https://www.youtube.com/watch?v=" + videoId;
            if (videoRepo.existsByUrl(url)) { ignorados++; continue; }

            YoutubeService.VideoInfo info = youtubeService.buscarInfoCompleta(videoId);
            VideoConhecimento video = new VideoConhecimento();
            video.setUrl(url);
            video.setTitulo(info.titulo());
            video.setTranscricao(info.transcricao());
            videoRepo.save(video);
            importados++;
            if (info.transcricao() != null) comTranscricao++;
        }

        return Map.of("importados", importados, "comTranscricao", comTranscricao, "ignorados", ignorados);
    }

    public void deletar(Long id) {
        if (!videoRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        videoRepo.deleteById(id);
    }
}
