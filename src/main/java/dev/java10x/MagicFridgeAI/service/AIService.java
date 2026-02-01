package dev.java10x.MagicFridgeAI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AIService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> generateRecipe() {
        String prompt = "Me sugira uma receita simples com ingredientes comuns.";

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of(
                        "text", prompt
                ))
            ))
        );

        return webClient.post()
            .header("Content-Type", "application/json")
            .header("x-goog-api-key", apiKey)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                try {
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");

                    Map<String, Object> firstCandidate = candidates.get(0);

                    Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");

                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

                    return (String) parts.get(0).get("text");

                } catch (Exception e) {
                    return "Erro ao processar resposta do Gemini: " + e.getMessage();
                }
            });
    }
}
