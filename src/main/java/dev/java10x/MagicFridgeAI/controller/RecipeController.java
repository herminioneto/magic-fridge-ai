package dev.java10x.MagicFridgeAI.controller;


import dev.java10x.MagicFridgeAI.model.FoodItem;
import dev.java10x.MagicFridgeAI.service.AIService;
import dev.java10x.MagicFridgeAI.service.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final FoodItemService foodItemService;
    private final AIService aiService;

    public RecipeController(FoodItemService foodItemService, AIService aiService) {
        this.foodItemService = foodItemService;
        this.aiService = aiService;
    }

    @GetMapping("/generate")
    public Mono<ResponseEntity<String>> generateRecipe() {
        List<FoodItem> foodItems = foodItemService.findAll();
        return aiService.generateRecipe(foodItems)
                .map(recipe -> ResponseEntity.ok(recipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
