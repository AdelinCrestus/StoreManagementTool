package com.storeManagementTool.StoreManagementTool.controllers;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.services.ProductService;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/store/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductAddDTO productAddDTO) { /// TODO: case with negatives values for q/
        return ResponseEntity.ok(productService.save(productAddDTO));
    }

    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(productDTO));
    }

    @PatchMapping({"/changePrice/{id}"})
    public ResponseEntity<ProductDTO> changePrice(@RequestParam Double price, @PathVariable Long id) {
        if( price < 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        return ResponseEntity.ok(productService.changePriceById(id, price));
    }

    @PatchMapping({"/changeQuantity/{id}"})
    public ResponseEntity<ProductDTO> changeQuantity(@RequestParam Integer quantity, @PathVariable Long id) {
        if( quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return ResponseEntity.ok(productService.changeQuantityById(id, quantity));
    }

    //TODO: To test what happens when I try to update a product that don't exists

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam @Nullable String name, @RequestParam @Nullable String description) {
        return ResponseEntity.ok(productService.findAll(name, description));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PatchMapping("/addToCart/{id}")
    public ResponseEntity<ProductDTO> addProductToCartById(@PathVariable Long id, @RequestParam Integer quantity,
                                                           @AuthenticationPrincipal UserEntity user) {
        log.info(user.getUsername());
        return ResponseEntity.ok(productService.addProductToCartById(id, quantity, user));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Product with id " + id + " deleted successfully.");
    }
}
