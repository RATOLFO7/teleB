package com.example.telegrambot;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String getResponseFromChatGPT(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONArray messages = new JSONArray();
        JSONObject userMessageObj = new JSONObject();
        userMessageObj.put("role", "user");
        userMessageObj.put("content", userMessage);
        messages.put(userMessageObj);

        JSONObject body = new JSONObject();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", messages);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray choices = jsonResponse.getJSONArray("choices");
                String botResponse = choices.getJSONObject(0).getJSONObject("message").getString("content");
                return botResponse;
            } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                return "Has excedido tu cuota de uso de la API de OpenAI. Por favor, revisa tu plan y detalles de facturación.";
            } else {
                return "Error al obtener la respuesta de OpenAI: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Excepción al obtener la respuesta de OpenAI: " + e.getMessage();
        }
    }
}
