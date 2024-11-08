package com.example.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageHandler {

    @Autowired
    private OpenAIService OpenAIService;

    public String processMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String userMessage = message.getText();
            return OpenAIService.getResponseFromChatGPT(userMessage);
        }
        return "No se ha encontrado un mensaje válido.";
    }
}
