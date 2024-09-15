package com.storeManagementTool.StoreManagementTool.controllers;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.services.CartService;
import com.storeManagementTool.StoreManagementTool.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal UserEntity user, @PathVariable Long id) {
        productService.deleteProductFromCartById(id, user);
        return ResponseEntity.ok("Deleted product from cart");
    }

    @PatchMapping("/purchase")
    public ResponseEntity<CartDTO> purchase(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(cartService.purchase(user));
    }


}
