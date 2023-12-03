package com.example.shoppinglist;

import com.example.shoppinglist.items.ShoppingItem;
import com.example.shoppinglist.repository.ShoppingItemsRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppinglistApplicationTests {

    @Autowired
    private MockMvc shoppingListMock;

    @Autowired
    private ShoppingItemsRepository shoppingItemsRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testAddNewItem() throws Exception {
        ShoppingItem expected = new ShoppingItem("water", 123.11, 2);
        String requestBody = "{ \"name\": \"water\", \"price\": 123.11, \"amount\": 2 }";
        MvcResult result = shoppingListMock
                .perform(post("/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Optional<ShoppingItem> savedItem = shoppingItemsRepository.findById(id);
        Assertions.assertTrue(savedItem.isPresent());
        Assertions.assertEquals(savedItem.get().getName(), expected.getName());
        Assertions.assertEquals(savedItem.get().getAmount(), expected.getAmount());
        Assertions.assertEquals(savedItem.get().getPrice(), expected.getPrice());

        result = shoppingListMock
                .perform(get("/list/" + id))
                .andReturn();
        Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.name"), expected.getName());
        Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.amount"), expected.getAmount());
        Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.price"), expected.getPrice());
    }

    @Test
    void testGetNotExistingItem() throws Exception {
        MvcResult result = shoppingListMock
                .perform(get("/list/" + 123))
                .andReturn();
        Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.message"),
                "Element with id = '123' not found");
    }

    @Test
    void testDeleteItem() throws Exception {
        String requestBody = "{ \"name\": \"water\", \"price\": 123.11, \"amount\": 2 }";
        MvcResult result = shoppingListMock
                .perform(post("/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Assertions.assertTrue(shoppingItemsRepository.findById(id).isPresent());

        shoppingListMock.perform(delete("/list/" + id));
        Assertions.assertTrue(shoppingItemsRepository.findById(id).isEmpty());
    }

    @Test
    void testUpdateItem() throws Exception {
        String requestBody = "{ \"name\": \"water\", \"price\": 123.11, \"amount\": 2 }";
        MvcResult result = shoppingListMock
                .perform(post("/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Assertions.assertTrue(shoppingItemsRepository.findById(id).isPresent());

        requestBody = "{ \"name\": \"milk\", \"price\": 22.2, \"amount\": 3 }";
        shoppingListMock
                .perform(post("/list/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        ShoppingItem actualItem = shoppingItemsRepository.findById(id).get();
        ShoppingItem expectedItem = new ShoppingItem(id, "milk", 22.2, 3);

        Assertions.assertTrue(shoppingItemsRepository.findById(id).isPresent());
        Assertions.assertEquals(expectedItem, actualItem);
    }

}
