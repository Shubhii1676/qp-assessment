package com.qp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.qp.model.GroceryItem;
import com.qp.repository.GroceryItemRepository;

@RestController
public class GroceryItemController {
	
	@Autowired
    private GroceryItemRepository groceryItemRepository;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/grocery-items")
    public List<GroceryItem> getAllGroceryItems() {
        return groceryItemRepository.findAll();
    }

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/grocery-items/{id}")
    public GroceryItem getGroceryItemById(@PathVariable Long id) {
        return groceryItemRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Grocery item with ID " + id + " not found"));
    }

	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/grocery-items")
    public GroceryItem addGroceryItem(@RequestBody GroceryItem groceryItem) {
        return groceryItemRepository.save(groceryItem);
    }

	@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/grocery-items/{id}")
    public GroceryItem updateGroceryItem(@PathVariable Long id, @RequestBody GroceryItem groceryItem) {
        groceryItem.setId(id);
        return groceryItemRepository.save(groceryItem);
    }

	@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/grocery-items/{id}")
    public void deleteGroceryItem(@PathVariable Long id) {
        groceryItemRepository.deleteById(id);
    }
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/grocery-items/{id}/inventory")
	public void updateGroceryItemInventory(@PathVariable Long id, @RequestParam Integer quantity) {
	    GroceryItem groceryItem = groceryItemRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Grocery item with ID " + id + " not found"));
	    groceryItem.setQuantity(quantity);
	    groceryItemRepository.save(groceryItem);
	}
}
