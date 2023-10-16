package com.qp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qp.model.GroceryItem;
import com.qp.model.Order;
import com.qp.model.User;
import com.qp.repository.GroceryItemRepository;
import com.qp.repository.OrderRepository;
import com.qp.repository.UserRepository;

@RestController
public class OrderController {
		
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GroceryItemRepository groceryItemRepository;
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/create-order/{userId}")
	public Order createOrder(@RequestBody List<GroceryItem> groceryItems, @PathVariable Long userId) {
	    Order order = new Order();
	    Optional<User> user = userRepository.findById(userId);
	    if(user.isPresent()) {
		    order.setGroceryItems(groceryItems);
		    List<GroceryItem> updatedGroceryItems = new ArrayList<>();
		    for(GroceryItem gitem : groceryItems) {
		    	GroceryItem gitemStock = groceryItemRepository.findByName(gitem.getName());
		    	if(gitemStock.getQuantity() >= gitem.getQuantity()) {
		    		gitemStock.setQuantity(gitemStock.getQuantity() - gitem.getQuantity());
		    		updatedGroceryItems.add(gitemStock);
		    	}
		    }
		    if(!updatedGroceryItems.isEmpty()) {
		    	groceryItemRepository.saveAllAndFlush(updatedGroceryItems);
		    }
		    return orderRepository.saveAndFlush(order);
	    }else {
	    	throw new IllegalArgumentException("something-went-wrong-please-try-again");
	    }
	}
}
