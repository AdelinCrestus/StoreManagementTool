package com.storeManagementTool.StoreManagementTool.services;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.mappers.ProductMapper;
import com.storeManagementTool.StoreManagementTool.repositories.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartService {

    private final ProductMapper productMapper;
    private final CartRepository cartRepository;

    public CartService(ProductMapper productMapper, CartRepository cartRepository) {
        this.productMapper = productMapper;
        this.cartRepository = cartRepository;
    }

    public CartDTO getCart(UserEntity user) {
        return productMapper.entityToDto(user.getCart());
    }

    public CartDTO purchase(UserEntity user) {

        CartDTO cartDTO = productMapper.entityToDto(user.getCart());
        user.getCart().getProducts().clear();
        log.info("Cart cleared, command was send");
        cartRepository.save(user.getCart());
        return cartDTO;
    }
}
