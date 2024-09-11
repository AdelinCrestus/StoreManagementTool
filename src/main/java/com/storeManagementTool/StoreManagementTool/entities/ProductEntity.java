package com.storeManagementTool.StoreManagementTool.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
