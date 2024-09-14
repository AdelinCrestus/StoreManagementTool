package com.storeManagementTool.StoreManagementTool.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CartDTO {
    private List<ProductDTO> products;
    private Double totalPrice;
}
