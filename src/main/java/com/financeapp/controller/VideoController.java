package com.financeapp.controller;

import com.financeapp.dto.VideoRequest;
import com.financeapp.dto.VideoResponse;
import com.financeapp.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public List<VideoResponse> listar() {
        return videoService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VideoResponse adicionar(@Valid @RequestBody VideoRequest request) {
        return videoService.adicionar(request);
    }

    @PostMapping("/canal")
    public Map<String, Integer> importarCanal(@RequestBody Map<String, String> body) {
        return videoService.importarCanal(body.get("url"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        videoService.deletar(id);
    }
}
