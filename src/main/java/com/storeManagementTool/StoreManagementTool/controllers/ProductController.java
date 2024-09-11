package com.storeManagementTool.StoreManagementTool.controllers;

import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductAddDTO productAddDTO) {
        return ResponseEntity.ok(productService.save(productAddDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }
}
