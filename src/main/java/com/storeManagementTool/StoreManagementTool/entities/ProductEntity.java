package com.storeManagementTool.StoreManagementTool.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private boolean inCart;
}
