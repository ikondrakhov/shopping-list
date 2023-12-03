package com.example.shoppinglist.repository;

import com.example.shoppinglist.items.ShoppingItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для взаимодействия с Таблицей элементов списка покупок
 */
@Repository
public interface ShoppingItemsRepository extends CrudRepository<ShoppingItem, Integer> {

    /**
     * Метод для получения списка покупок
     * @return список покупок
     */
    List<ShoppingItem> findAll();

    /**
     * Метод для удаления элемента списка
     * @param id id элемента
     */
    void deleteById(Integer id);
}
