package com.storeManagementTool.StoreManagementTool.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductAddDTO {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
