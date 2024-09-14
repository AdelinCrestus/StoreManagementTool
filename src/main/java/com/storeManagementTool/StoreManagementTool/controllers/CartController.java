package com.storeManagementTool.StoreManagementTool.controllers;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store/cart")
public class CartController {
    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/finishOrder")
    public ResponseEntity<CartDTO> finishOrder(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(productService.finishOrder(user));
    }
}
