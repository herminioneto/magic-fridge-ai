package dev.java10x.MagicFridgeAI.controller;

import dev.java10x.MagicFridgeAI.model.FoodItem;
import dev.java10x.MagicFridgeAI.service.FoodItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @PostMapping()
    public ResponseEntity<FoodItem> create(@RequestBody FoodItem foodItem) {
        FoodItem newFoodItem = foodItemService.save(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newFoodItem);
    }

    @GetMapping()
    public ResponseEntity<List<FoodItem>> findAll() {
        List<FoodItem> foodItems = foodItemService.findAll();
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        FoodItem foodItem = foodItemService.findById(id);
        if (foodItem != null) {
            return ResponseEntity.ok(foodItem);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food with id " + id + " was not found.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody FoodItem foodItemUpdatedData) {
        FoodItem foodItemUpdated = foodItemService.updateById(id, foodItemUpdatedData);

        if (foodItemUpdated != null) {
            return ResponseEntity.ok(foodItemUpdated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food with id " + id + " was not found.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        FoodItem foodToDelete = foodItemService.findById(id);

        if (foodToDelete != null) {
            foodItemService.deleteById(id);
            return ResponseEntity.ok("Food with id " + id + " deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food with id " + id + " was not found.");
        }
    }
}
