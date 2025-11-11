package dev.java10x.MagicFridgeAI.service;

import dev.java10x.MagicFridgeAI.model.FoodItem;
import dev.java10x.MagicFridgeAI.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public FoodItem save(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    public List<FoodItem> findAll() {
        return foodItemRepository.findAll();
    }

    public FoodItem findById(Long id) {
        Optional<FoodItem> foodItem = foodItemRepository.findById(id);
        return foodItem.orElse(null);
    }

    public FoodItem updateById(Long id, FoodItem foodItemUpdated) {
        if (foodItemRepository.existsById(id)) {
            foodItemUpdated.setId(id);
            return foodItemRepository.save(foodItemUpdated);
        }
        return null;
    }

    public void deleteById(Long id) {
        foodItemRepository.deleteById(id);
    }
}
