package com.storeManagementTool.StoreManagementTool.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
