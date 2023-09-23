package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class CartControllerTest {
	
	private CartController cartController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddTocart() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(cartRepository.save(any())).thenReturn(new Cart());
        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("user");
        ResponseEntity<Cart> cartResponse = cartController.addTocart(modifyCartRequest);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());
    }

    @Test
    public void testAddCartWithUserNotFound() {
        when(userRepository.findByUsername("user")).thenReturn(null);
        ResponseEntity<Cart> cartResponse = cartController.addTocart(new ModifyCartRequest());
        assertNotNull(cartResponse);
        assertEquals(404, cartResponse.getStatusCodeValue());
    }

    @Test
    public void testAddToCartWithItemNotFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user")).thenReturn(user);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("user");
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> cartResponse = cartController.addTocart(modifyCartRequest);
        assertNotNull(cartResponse);
        assertEquals(404, cartResponse.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCart() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(cartRepository.save(any())).thenReturn(new Cart());
        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setUsername("user");
        ResponseEntity<Cart> cartResponse = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCartWithUserNotFound() {
        when(userRepository.findByUsername("user name")).thenReturn(null);
        ResponseEntity<Cart> cartResponse = cartController.removeFromcart(new ModifyCartRequest());
        assertNotNull(cartResponse);
        assertEquals(404, cartResponse.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCartWithItemNotFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user")).thenReturn(user);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("user");
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> cartResponse = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(cartResponse);
        assertEquals(404, cartResponse.getStatusCodeValue());
    }
}
