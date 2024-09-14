package com.storeManagementTool.StoreManagementTool.services;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.exceptions.InsufficientQuantityException;
import com.storeManagementTool.StoreManagementTool.exceptions.ProductNotFoundException;
import com.storeManagementTool.StoreManagementTool.mappers.ProductMapper;
import com.storeManagementTool.StoreManagementTool.repositories.CartRepository;
import com.storeManagementTool.StoreManagementTool.repositories.ProductRepository;
import com.storeManagementTool.StoreManagementTool.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public ProductService(ProductMapper productMapper, ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public List<ProductDTO> findAll(String name, String description) {
        if (name == null && description == null) {
            return productRepository.findAll()
                    .stream()
                    .filter(prod -> !prod.isInCart())
                    .map(productMapper::entityToDto)
                    .collect(Collectors.toList());
        } else if (description != null && name == null) {
            return productRepository.findProductEntitiesByDescriptionContainingIgnoreCase(description)
                    .stream()
                    .filter(prod -> !prod.isInCart())
                    .map(productMapper::entityToDto)
                    .collect(Collectors.toList());
        } else if (description == null) {
            return productRepository.findProductEntitiesByNameContainingIgnoreCase(name)
                    .stream()
                    .filter(prod -> !prod.isInCart())
                    .map(productMapper::entityToDto)
                    .collect(Collectors.toList());
        }
        return productRepository.findProductEntitiesByDescriptionContainingIgnoreCaseAndNameContainingIgnoreCase(description, name)
                .stream()
                .filter(prod -> !prod.isInCart())
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
            log.info("Product with id: {} was updated", productEntity.getId());
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
            log.info("Price of product with id: {} was changed to {}", productEntity.getId(), productEntity.getPrice());
            return productMapper.entityToDto(productRepository.save(productEntity));
        }
        throw new ProductNotFoundException();
    }

    public ProductDTO changeQuantityById(Long id, Integer quantity) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (productEntityOptional.isPresent()) {
            ProductEntity productEntity = productEntityOptional.get();
            productEntity.setQuantity(quantity);
            log.info("Quantity of product with id: {} was changed to {}", productEntity.getId(), quantity);
            return productMapper.entityToDto(productRepository.save(productEntity));
        }
        throw new ProductNotFoundException();
    }

    @Transactional
    public ProductDTO addProductToCartById(Long id, Integer quantity, UserEntity user) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        user = userRepository.findByUsername(user.getUsername()).orElse(null);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (productEntityOptional.isEmpty()) {
            throw new ProductNotFoundException();
        }
        ProductEntity productEntity = productEntityOptional.get();
        if (productEntity.getQuantity() < quantity) {
            throw new InsufficientQuantityException();
        }

        productEntity.setQuantity(productEntity.getQuantity() - quantity);
        ProductEntity addedProduct = ProductEntity.builder()
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .quantity(quantity)
                .inCart(true)
                .build();
        user.getCart().getProducts().add(
               addedProduct
        );
        userRepository.save(user);
        productRepository.save(productEntity);
        addedProduct.setId(productEntity.getId());
        log.info("Product with id: {} was added to cart", addedProduct.getId());
        return productMapper.entityToDto(addedProduct);
    }

    public void deleteProductFromCartById(Long id, UserEntity user) {
//        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
//                () -> new UsernameNotFoundException("User not found")
//        );
        ProductEntity productEntityToDelete = user
                .getCart()
                .getProducts()
                .stream()
                .filter(product -> Objects.equals(product.getId(), id))
                .findFirst()
                .orElseThrow(ProductNotFoundException::new);
        Integer quantity = productEntityToDelete.getQuantity();
        ProductEntity productEntity = productRepository
                .findAll()
                .stream()
                .filter(product -> product.getName().equals(productEntityToDelete.getName()))
                .filter(product -> !product.isInCart())
                .findFirst()
                .orElseThrow();
        productEntity.setQuantity(productEntity.getQuantity() + quantity);
        productRepository.save(productEntity);
        user.getCart().getProducts().remove(productEntityToDelete);
        cartRepository.save(user.getCart());
        log.info("Product with id: {} was deleted from cart", productEntity.getId());
        delete(id);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
