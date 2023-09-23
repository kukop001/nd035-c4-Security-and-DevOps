package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class OrderControllerTest {
	
	private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);
    
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void testSubmit() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user")).thenReturn(user);
        Optional<UserOrder> order = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(order);

        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal(10.0));
        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(new BigDecimal(10.0));
        cart.setItems((items));
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<UserOrder> userOrderResponse = orderController.submit("user");
        assertNotNull(userOrderResponse);
        assertEquals(200, userOrderResponse.getStatusCodeValue());
    }

    @Test
    public void testSubmitFailed() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user")).thenReturn(null);
        ResponseEntity<UserOrder> userOrderResponse = orderController.submit("user");
        assertNotNull(userOrderResponse);
        assertEquals(404, userOrderResponse.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user")).thenReturn(user);
        Optional<UserOrder> userOrder = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(userOrder);
        ResponseEntity<List<UserOrder>> userOrderResponse = orderController.getOrdersForUser("user");
        assertNotNull(userOrderResponse);
        assertEquals(200, userOrderResponse.getStatusCodeValue());
    }

    @Test
    public void testGetOrderByInvalidUsername() {
        User userMock = new User();
        userMock.setUsername("username");
        when(userRepository.findByUsername("username")).thenReturn(null);

        ResponseEntity<List<UserOrder>> userOrderResponse = orderController.getOrdersForUser("username");
        assertNotNull(userOrderResponse);
        assertEquals(404, userOrderResponse.getStatusCodeValue());
    }
}
