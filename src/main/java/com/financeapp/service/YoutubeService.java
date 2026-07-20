package com.financeapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class YoutubeService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String extrairVideoId(String url) {
        Pattern p = Pattern.compile("(?:youtube\\.com/(?:watch\\?v=|shorts/)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
        Matcher m = p.matcher(url);
        return m.find() ? m.group(1) : null;
    }

    public String buscarTitulo(String videoId) {
        try {
            String oembedUrl = "https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=" + videoId + "&format=json";
            Map<?, ?> res = restTemplate.getForObject(oembedUrl, Map.class);
            return res != null ? (String) res.get("title") : "Vídeo " + videoId;
        } catch (Exception e) {
            return "Vídeo " + videoId;
        }
    }

    public record VideoInfo(String titulo, String transcricao) {}

    public List<String> buscarVideosDoCanal(String channelUrl) {
        try {
            String base = channelUrl.replaceAll("/@([^/?#]+).*", "/@$1");
            String videosUrl = base + "/videos";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            headers.set("Accept-Language", "pt-BR,pt;q=0.9,en;q=0.8");
            HttpEntity<?> entity = new HttpEntity<>(headers);

            String html = restTemplate.exchange(videosUrl, HttpMethod.GET, entity, String.class).getBody();
            if (html == null) return List.of();

            Pattern pattern = Pattern.compile("\"videoId\":\"([a-zA-Z0-9_-]{11})\"");
            Matcher matcher = pattern.matcher(html);
            Set<String> ids = new LinkedHashSet<>();
            while (matcher.find()) ids.add(matcher.group(1));

            return new ArrayList<>(ids);
        } catch (Exception e) {
            log.warn("Falha ao buscar vídeos do canal: {}", e.getMessage());
            return List.of();
        }
    }

    public VideoInfo buscarInfoCompleta(String videoId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            headers.set("Accept-Language", "pt-BR,pt;q=0.9,en;q=0.8");
            HttpEntity<?> entity = new HttpEntity<>(headers);

            String html = restTemplate.exchange(
                "https://www.youtube.com/watch?v=" + videoId, HttpMethod.GET, entity, String.class
            ).getBody();
            if (html == null) return new VideoInfo("Vídeo " + videoId, null);

            // Título
            String titulo = "Vídeo " + videoId;
            Matcher titleMatcher = Pattern.compile("<title>(.+?) - YouTube</title>").matcher(html);
            if (titleMatcher.find()) titulo = titleMatcher.group(1).trim();

            // Transcrição
            String transcricao = null;
            Matcher captionMatcher = Pattern.compile("\"captionTracks\":\\[\\{\"baseUrl\":\"([^\"]+)\"").matcher(html);
            if (captionMatcher.find()) {
                String captionUrl = captionMatcher.group(1).replace("\\u0026", "&");
                try {
                    String xml = restTemplate.getForObject(captionUrl, String.class);
                    if (xml != null) {
                        Matcher textMatcher = Pattern.compile("<text[^>]*>([^<]*)</text>").matcher(xml);
                        StringBuilder sb = new StringBuilder();
                        while (textMatcher.find()) {
                            String t = textMatcher.group(1)
                                .replace("&amp;", "&").replace("&lt;", "<")
                                .replace("&gt;", ">").replace("&quot;", "\"")
                                .replace("&#39;", "'").trim();
                            if (!t.isEmpty()) sb.append(t).append(" ");
                        }
                        if (!sb.toString().isBlank()) transcricao = sb.toString().trim();
                    }
                } catch (Exception ignored) {}
            }
            return new VideoInfo(titulo, transcricao);
        } catch (Exception e) {
            log.warn("Falha ao buscar info do vídeo {}: {}", videoId, e.getMessage());
            return new VideoInfo("Vídeo " + videoId, null);
        }
    }

    public String buscarTranscricao(String url) {
        try {
            String videoId = extrairVideoId(url);
            if (videoId == null) return null;

            String pageHtml = restTemplate.getForObject("https://www.youtube.com/watch?v=" + videoId, String.class);
            if (pageHtml == null) return null;

            Pattern captionPattern = Pattern.compile("\"captionTracks\":\\[\\{\"baseUrl\":\"([^\"]+)\"");
            Matcher captionMatcher = captionPattern.matcher(pageHtml);
            if (!captionMatcher.find()) return null;

            String captionUrl = captionMatcher.group(1).replace("\\u0026", "&");
            String xml = restTemplate.getForObject(captionUrl, String.class);
            if (xml == null) return null;

            Pattern textPattern = Pattern.compile("<text[^>]*>([^<]*)</text>");
            Matcher textMatcher = textPattern.matcher(xml);
            StringBuilder sb = new StringBuilder();
            while (textMatcher.find()) {
                String text = textMatcher.group(1)
                    .replace("&amp;", "&").replace("&lt;", "<")
                    .replace("&gt;", ">").replace("&quot;", "\"")
                    .replace("&#39;", "'").trim();
                if (!text.isEmpty()) sb.append(text).append(" ");
            }
            return sb.toString().isBlank() ? null : sb.toString().trim();
        } catch (Exception e) {
            log.warn("Falha ao buscar transcrição do YouTube: {}", e.getMessage());
            return null;
        }
    }
}
