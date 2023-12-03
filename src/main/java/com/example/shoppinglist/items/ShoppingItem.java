package com.example.shoppinglist.items;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс элемент списка покупок
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_items")
public class ShoppingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;
    private Integer amount;

    public ShoppingItem(String name, Double price, Integer amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
}
