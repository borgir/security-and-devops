package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class OrderControllerTests {

    private OrderController orderController;

    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"orderRepository", orderRepo);
        TestUtils.injectObjects(orderController,"userRepository", userRepo);
    }

    /**
     *
     */
    @Test
    public void order_submit_happy_path() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Become a Java Web Developer - Nanodegree Program");
        item.setPrice(new BigDecimal(1000));

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        user.setUsername("johndoe");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(item.getPrice());

        user.setCart(cart);

        when(userRepo.findByUsername("johndoe")).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("johndoe");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertEquals(user, order.getUser());
        assertEquals(items, order.getItems());
        assertEquals(item.getPrice(), order.getTotal());

    }

    /**
     *
     */
    @Test
    public void get_orders_for_user_happy_path() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Become a Java Web Developer - Nanodegree Program");
        item.setPrice(new BigDecimal(1000));

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        user.setUsername("johndoe");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(item.getPrice());

        user.setCart(cart);

        orderController.submit("johndoe");
        when(userRepo.findByUsername("johndoe")).thenReturn(user);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("johndoe");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);

    }

}
