package dev.java10x.MagicFridgeAI.service;

import dev.java10x.MagicFridgeAI.model.FoodItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AIService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> generateRecipe(List<FoodItem> foodItems) {

        String ingredients = foodItems.stream()
            .map(item -> String.format("%s (%s) - Quantidade: %d, Validade: %s",
                item.getName(), item.getCategory(), item.getQuantity(), item.getExpirationDate()))
            .collect(Collectors.joining("\n"));
            ;
        String prompt = "Me sugira uma receita simples com os seguintes ingredientes:\n " + ingredients;

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
