package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTests {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    /**
     *
     */
    @Test
    public void add_to_cart_happy_path() {

        Cart userCart = new Cart();

        User user = new User();
        user.setUsername("johndoe");
        user.setCart(userCart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Become a Java Web Developer - Nanodegree Program");
        item.setPrice(new BigDecimal(1000));
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);
        listItems.add(item);

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("johndoe");
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(2);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Cart cart = responseEntity.getBody();

        assertNotNull(cart);
        assertEquals(new BigDecimal(2000), cart.getTotal());
        assertEquals(listItems, cart.getItems());

    }

    /**
     *
     */
    @Test
    public void remove_from_cart_happy_path() {

        Cart userCart = new Cart();

        User user = new User();
        user.setUsername("johndoe");
        user.setCart(userCart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Become a Java Web Developer - Nanodegree Program");
        item.setPrice(new BigDecimal(1000));
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("johndoe");

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart cart = response.getBody();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(0, cart.getItems().size());

    }

}
