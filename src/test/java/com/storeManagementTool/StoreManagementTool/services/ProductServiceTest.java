package com.storeManagementTool.StoreManagementTool.services;

import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import com.storeManagementTool.StoreManagementTool.mappers.ProductMapper;
import com.storeManagementTool.StoreManagementTool.repositories.CartRepository;
import com.storeManagementTool.StoreManagementTool.repositories.ProductRepository;
import com.storeManagementTool.StoreManagementTool.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private ProductService productService;

    private static List<ProductEntity> products;

    @BeforeAll
    public static void setUp() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Product1");
        productEntity.setInCart(false);
        productEntity.setDescription("Description1");
        productEntity.setQuantity(10);
        productEntity.setPrice(10.0);

        ProductEntity productEntity2 = new ProductEntity();
        productEntity.setId(2L);
        productEntity.setName("Product2");
        productEntity.setInCart(false);
        productEntity.setDescription("Description of Product 2");
        productEntity.setQuantity(100);
        productEntity.setPrice(100.0);

        products = Arrays.asList(productEntity, productEntity2);
    }

    @Test
    public void testFindAll_WhenNameAndDescriptionAreNull() {
        // Arrange
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.entityToDto(any(ProductEntity.class))).thenAnswer(invocation -> {
            ProductEntity entity = invocation.getArgument(0);
            return ProductDTO.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .build();
        });

        // Act
        List<ProductDTO> products = productService.findAll(null, null);

        // Assert
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }


}