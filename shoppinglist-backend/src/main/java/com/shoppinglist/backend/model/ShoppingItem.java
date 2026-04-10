package com.shoppinglist.backend.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "shopping_items")
public class ShoppingItem extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public boolean checked;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id", nullable = false)
    public ShoppingList shoppingList;

    @Column(nullable = false)
    public Instant createdAt;

    @Column(nullable = false)
    public Instant updatedAt;

    public ShoppingItem() {
    }

    public ShoppingItem(String name, ShoppingList shoppingList) {
        this.name = name;
        this.checked = false;
        this.shoppingList = shoppingList;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
