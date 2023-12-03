package com.example.shoppinglist.controller;

import com.example.shoppinglist.exceptions.ExceptionWrapper;
import com.example.shoppinglist.items.ShoppingItem;
import com.example.shoppinglist.repository.ShoppingItemsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Класс контроллер списка покупок
 */
@RestController
@RequestMapping("/list")
@Slf4j
public class ShoppingListController {

    @Autowired
    private ShoppingItemsRepository shoppingItemsRepository;

    /**
     * Метод для добавления элемента списка
     *
     * @param shoppingItem новый элемент списка
     * @return Добавленный элемент списка
     */
    @PostMapping
    public ShoppingItem postItem(@RequestBody ShoppingItem shoppingItem) {
        log.info(String.format("Добавление эелемента %s", shoppingItem.toString()));
        return shoppingItemsRepository.save(shoppingItem);
    }

    /**
     * Метод для удаление элемента списка
     *
     * @param id id удаляемого элемента списка
     * @return Добавленный элемент списка
     */
    @DeleteMapping("/{id}")
    public ShoppingItem deleteItem(@PathVariable Integer id) throws ExceptionWrapper {
        log.info(String.format("Удаление эелмента с id = %s", id));
        ShoppingItem item;
        try {
            item = shoppingItemsRepository.findById(id).get();
            shoppingItemsRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ExceptionWrapper(String.format("No element with id = %s", id));
        }
        return item;
    }

    /**
     * Метод для обновления элемента списка
     *
     * @param shoppingItem элемент списка
     * @return Обновленный элемент списка
     */
    @PostMapping("/update/{id}")
    public ShoppingItem updateItem(@PathVariable Integer id, @RequestBody ShoppingItem shoppingItem)
            throws ExceptionWrapper {
        log.info(String.format("Обновление элемента с id = %s ", id));
        log.info(String.format("Новые данные %s", shoppingItem.toString()));

        Optional<ShoppingItem> item = shoppingItemsRepository.findById(id);
        if (item.isEmpty()) {
            throw new ExceptionWrapper(String.format("Element with id = '%s' not found", id));
        }
        item.get().setAmount(shoppingItem.getAmount());
        item.get().setName(shoppingItem.getName());
        item.get().setPrice(shoppingItem.getPrice());
        shoppingItemsRepository.save(item.get());

        return item.get();
    }

    /**
     * Метод для получения элемента списка
     *
     * @param id id элемента списка
     * @return Элемент списка
     */
    @GetMapping("/{id}")
    public ShoppingItem getItem(@PathVariable Integer id) throws ExceptionWrapper {
        log.info(String.format("Получаем элемент с id = %s", id));

        try {
            return shoppingItemsRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ExceptionWrapper(String.format("Element with id = '%s' not found", id));
        }
    }

    /**
     * Метод для получения списка покупок
     *
     * @return Список элементов
     */
    @GetMapping
    public List<ShoppingItem> getShoppingList() {
        log.info("Получаем список покупок");
        return shoppingItemsRepository.findAll();
    }

    @ExceptionHandler(value = ExceptionWrapper.class)
    @ResponseBody
    @ResponseStatus(code= HttpStatus.NOT_FOUND)
    private Object itemNotFoundException(ExceptionWrapper e) {
        log.error("Ошибка: " + e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<Object>(e.toString(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    private Object unknownException(Exception e) {
        log.error("Ошибка: " + e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<Object>(new ExceptionWrapper(e.getMessage()).toString(),
                headers, HttpStatus.BAD_REQUEST);
    }
}
