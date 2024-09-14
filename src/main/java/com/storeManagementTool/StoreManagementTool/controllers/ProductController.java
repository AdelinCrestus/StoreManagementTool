package com.storeManagementTool.StoreManagementTool.controllers;

import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.Role;
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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductAddDTO productAddDTO,
                                                    @AuthenticationPrincipal UserEntity user) { /// TODO: case with negatives values for q/
        if (user.getRole() != Role.ADMIN) {
            log.error("Only admins can create products");
            throw new IllegalArgumentException("Only admins can create products");
        }

        if (productAddDTO.getPrice() < 0 || productAddDTO.getQuantity() < 0) {
            log.error("Price or quantity are negative");
            throw new IllegalArgumentException("Price or quantity must be greater than 0");
        }

        return ResponseEntity.ok(productService.save(productAddDTO));
    }

    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,
                                                    @AuthenticationPrincipal UserEntity user) {
        if (user.getRole() != Role.ADMIN) {
            log.error("Only admins can update products");
            throw new IllegalArgumentException("Only admins can create products");
        }
        return ResponseEntity.ok(productService.update(productDTO));
    }

    @PatchMapping({"/changePrice/{id}"})
    public ResponseEntity<ProductDTO> changePrice(@RequestParam Double price, @PathVariable Long id) {
        if( price < 0) {
            log.error("Price is negative");
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        return ResponseEntity.ok(productService.changePriceById(id, price));
    }

    @PatchMapping({"/changeQuantity/{id}"})
    public ResponseEntity<ProductDTO> changeQuantity(@RequestParam Integer quantity, @PathVariable Long id,
                                                     @AuthenticationPrincipal UserEntity user) {
        if (user.getRole() != Role.ADMIN) {
            log.error("Only admins can change quantity");
            throw new IllegalArgumentException("Only admins can change quantity");
        }

        if( quantity < 0) {
            log.error("Quantity is negative");
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return ResponseEntity.ok(productService.changeQuantityById(id, quantity));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam @Nullable String name,
                                                           @RequestParam @Nullable String description) {
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
    public ResponseEntity<String> deleteById(@PathVariable Long id, @AuthenticationPrincipal UserEntity user) {
        if (user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only admins can create products");
        }
        productService.delete(id);
        return ResponseEntity.ok("Product with id " + id + " deleted successfully.");
    }
}
