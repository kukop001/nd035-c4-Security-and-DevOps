package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {

	private ItemController itemController;

	private ItemRepository itemRepository = mock(ItemRepository.class);

	@Before
	public void setUp() {
		itemController = new ItemController();
		TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
	}

	@Test
	public void testGetItems() {
		ResponseEntity<List<Item>> items = itemController.getItems();
		assertNotNull(items);
		assertEquals(200, items.getStatusCodeValue());
	}

	@Test
	public void testGetItemById() {
		Item item = new Item();
		item.setId(1l);
		item.setPrice(new BigDecimal(10.0));
		when(itemRepository.findById(1l)).thenReturn(Optional.of(item));
		ResponseEntity<Item> itemResponse = itemController.getItemById(1l);
		assertNotNull(item);
		assertEquals(200, itemResponse.getStatusCodeValue());
	}

	@Test
	public void testGetItemsByName() {
		List<Item> items = new ArrayList<>();
		items.add(new Item());
		when(itemRepository.findByName("Item name")).thenReturn(items);
		ResponseEntity<List<Item>> itemsResponse = itemController.getItemsByName("Item name");
		assertNotNull(items);
		assertEquals(200, itemsResponse.getStatusCodeValue());
		assertEquals(1, itemsResponse.getBody().size());
	}
}
