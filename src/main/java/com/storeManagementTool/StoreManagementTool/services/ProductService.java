package com.storeManagementTool.StoreManagementTool.services;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.exceptions.InsufficientQuantityException;
import com.storeManagementTool.StoreManagementTool.exceptions.ProductNotFoundException;
import com.storeManagementTool.StoreManagementTool.mappers.ProductMapper;
import com.storeManagementTool.StoreManagementTool.repositories.ProductRepository;
import com.storeManagementTool.StoreManagementTool.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductMapper productMapper, ProductRepository productRepository, UserRepository userRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<ProductDTO> findAll(String name, String description) {
        if (name == null && description == null) {
            return productRepository.findAll().stream().map(productMapper::entityToDto).collect(Collectors.toList());
        } else if (description != null && name == null) {
            return productRepository.findProductEntitiesByDescriptionContainingIgnoreCase(description)
                    .stream()
                    .map(productMapper::entityToDto)
                    .collect(Collectors.toList());
        } else if (description == null) {
            return productRepository.findProductEntitiesByNameContainingIgnoreCase(name)
                    .stream()
                    .map(productMapper::entityToDto)
                    .collect(Collectors.toList());
        }
        return productRepository.findProductEntitiesByDescriptionContainingIgnoreCaseAndNameContainingIgnoreCase(description, name)
                .stream()
                .map(productMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if (productEntity.isPresent()) {
            return productMapper.entityToDto(productEntity.get());
        }
        throw new ProductNotFoundException();
    }

    public ProductDTO save(ProductAddDTO productAddDTO) {
        ProductEntity productEntity = productRepository.save(productMapper.dtoToEntity(productAddDTO));
        return productMapper.entityToDto(productEntity);
    }

    @Transactional
    public ProductDTO update(ProductDTO productDTO) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(productDTO.getId());
        if (productEntityOptional.isPresent()) {
            ProductEntity productEntity = productEntityOptional.get();
            productEntity.setName(productDTO.getName());
            productEntity.setDescription(productDTO.getDescription());
            productEntity.setPrice(productDTO.getPrice());
            productEntity.setQuantity(productDTO.getQuantity());
            productRepository.save(productEntity);
            return productDTO;
        }
        throw new ProductNotFoundException();
    }

    @Transactional
    public ProductDTO changePriceById(Long id, Double price) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (productEntityOptional.isPresent()) {
            ProductEntity productEntity = productEntityOptional.get();
            productEntity.setPrice(price);
            return productMapper.entityToDto(productRepository.save(productEntity));
        }
        throw new ProductNotFoundException();
    }

    public ProductDTO changeQuantityById(Long id, Integer quantity) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (productEntityOptional.isPresent()) {
            ProductEntity productEntity = productEntityOptional.get();
            productEntity.setQuantity(quantity);
            return productMapper.entityToDto(productRepository.save(productEntity));
        }
        throw new ProductNotFoundException();
    }

    public ProductDTO addProductToCartById(Long id, Integer quantity, UserEntity user) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (productEntityOptional.isEmpty()) {
            throw new ProductNotFoundException();
        }
        ProductEntity productEntity = productEntityOptional.get();
        if (productEntity.getQuantity() < quantity) {
            throw new InsufficientQuantityException();
        }

        productEntity.setQuantity(productEntity.getQuantity() - quantity);
        user.getCart().getProducts().add(productEntity);
        userRepository.save(user);
        return productMapper.entityToDto(productRepository.save(productEntity));
    }

    public CartDTO finishOrder(UserEntity user) {

        CartDTO cartDTO = productMapper.entityToDto(user.getCart());
        user.getCart().getProducts().clear();
        return cartDTO;
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
