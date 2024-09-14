package com.storeManagementTool.StoreManagementTool.services;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.mappers.ProductMapper;
import com.storeManagementTool.StoreManagementTool.repositories.CartRepository;
import com.storeManagementTool.StoreManagementTool.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public CartService(ProductMapper productMapper, UserRepository userRepository, CartRepository cartRepository) {
        this.productMapper = productMapper;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public CartDTO getCart(UserEntity user) {
//        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
//                () ->new UsernameNotFoundException("User not found"));
        return productMapper.entityToDto(user.getCart());
    }

    public CartDTO purchase(UserEntity user) {
//        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
//                () -> new UsernameNotFoundException("User not found")
//        );

        CartDTO cartDTO = productMapper.entityToDto(user.getCart());
        user.getCart().getProducts().clear();
        cartRepository.save(user.getCart());
        return cartDTO;
    }
}
