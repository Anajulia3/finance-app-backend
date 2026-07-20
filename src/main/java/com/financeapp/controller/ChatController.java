package com.financeapp.controller;

import com.financeapp.dto.ChatRequest;
import com.financeapp.dto.ChatResponse;
import com.financeapp.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return new ChatResponse(chatService.responder(request.pergunta()));
    }
}
