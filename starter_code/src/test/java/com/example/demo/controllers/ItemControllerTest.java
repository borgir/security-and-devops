package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.Item;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    /**
     *
     */
    @Test
    public void get_item_by_id_happy_path() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Become a Java Web Developer - Nanodegree Program");
        item.setPrice(new BigDecimal(1000));

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(item.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemFound = response.getBody();
        assertNotNull(itemFound);
        assertEquals(item.getId(), itemFound.getId());
        assertEquals("Become a Java Web Developer - Nanodegree Program", itemFound.getName());
        assertEquals(item.getPrice(), itemFound.getPrice());

    }

    /**
     *
     */
    @Test
    public void get_items_by_name_happy_path() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Product A");
        item.setPrice(new BigDecimal(1000));

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepo.findByName("Product A")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Product A");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemsFound = response.getBody();
        assertNotNull(itemsFound);
        assertEquals(item.getId(), itemsFound.get(0).getId());
        assertEquals(item.getName(), itemsFound.get(0).getName());
        assertEquals(item.getPrice(), itemsFound.get(0).getPrice());

    }

}
