package com.storeManagementTool.StoreManagementTool.services;

import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.CartEntity;
import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.exceptions.InsufficientQuantityException;
import com.storeManagementTool.StoreManagementTool.exceptions.ProductNotFoundException;
import com.storeManagementTool.StoreManagementTool.mappers.ProductMapper;
import com.storeManagementTool.StoreManagementTool.repositories.ProductRepository;
import com.storeManagementTool.StoreManagementTool.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private ProductService productService;

    private static List<ProductEntity> products;
    private static UserEntity user;

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
        productEntity2.setId(2L);
        productEntity2.setName("Product2");
        productEntity2.setInCart(false);
        productEntity2.setDescription("Description of Product 2");
        productEntity2.setQuantity(100);
        productEntity2.setPrice(100.0);

        products = Arrays.asList(productEntity, productEntity2);

        user = new UserEntity();
        user.setUsername("testUser");
        user.setCart(new CartEntity());
        user.getCart().setProducts(new ArrayList<>());
    }

    @BeforeEach
    public void mockMapper() {
        lenient().when(productMapper.entityToDto(any(ProductEntity.class))).thenAnswer(invocation -> {
            ProductEntity entity = invocation.getArgument(0);
            return ProductDTO.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .build() ;
        });
    }

    @Test
    public void testFindAll_WhenNameAndDescriptionAreNull() {
        
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> productsDTO = productService.findAll(null, null);

        assertNotNull(productsDTO);
        assertEquals(2, productsDTO.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_WhenDescriptionIsNotNullAndNameIsNull() {
        
        String description = "Product";

        when(productRepository.findProductEntitiesByDescriptionContainingIgnoreCase(description))
                .thenReturn(Collections.singletonList(products.getLast()));
        List<ProductDTO> productsDTO = productService.findAll(null, description);

        assertNotNull(productsDTO);
        assertEquals(1, productsDTO.size());
        assertTrue(productsDTO.getFirst().getDescription().contains(description));
        verify(productRepository, times(1)).findProductEntitiesByDescriptionContainingIgnoreCase(description);
    }

    @Test
    public void testFindAll_WhenNameIsNotNullAndDescriptionIsNull() {
        
        String name = "Product1";

        when(productRepository.findProductEntitiesByNameContainingIgnoreCase(name))
                .thenReturn(Collections.singletonList(products.getFirst()));

        List<ProductDTO> products = productService.findAll(name, null);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(name, products.getFirst().getName());
        verify(productRepository, times(1)).findProductEntitiesByNameContainingIgnoreCase(name);
    }

    @Test
    public void testFindAll_WhenNameAndDescriptionAreNotNull() {
        
        String name = "Product2";
        String description = "Description";
        when(productRepository.findProductEntitiesByDescriptionContainingIgnoreCaseAndNameContainingIgnoreCase(description, name))
                .thenReturn(Collections.singletonList(products.getLast()));

        List<ProductDTO> productsDTO = productService.findAll(name, description);

        assertNotNull(productsDTO);
        assertEquals(1, productsDTO.size());
        assertEquals(name, productsDTO.getFirst().getName());
        verify(productRepository, times(1))
                .findProductEntitiesByDescriptionContainingIgnoreCaseAndNameContainingIgnoreCase(description, name);
    }

    @Test
    public void testFindById_WhenProductExists() {
        
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.of(products.getFirst()));

        ProductDTO productDTO = productService.findById(productId);

        assertNotNull(productDTO);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testFindById_WhenProductDoesNotExist() {
        
        Long productId = 3L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testSaveProduct() {
        
        ProductAddDTO productAddDTO = new ProductAddDTO();
        ProductEntity productEntity = new ProductEntity();

        when(productMapper.dtoToEntity(productAddDTO)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        ProductDTO productDTO = productService.save(productAddDTO);

        assertNotNull(productDTO);
        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    public void testUpdate_WhenProductExists() {
        
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Updated Name");

        when(productRepository.findById(productDTO.getId())).thenReturn(Optional.of(products.getFirst()));

        ProductDTO updatedProductDTO = productService.update(productDTO);

        assertNotNull(updatedProductDTO);
        assertEquals("Updated Name", products.getFirst().getName());
        verify(productRepository, times(1)).save(products.getFirst());
    }

    @Test
    public void testUpdate_WhenProductDoesNotExist() {
        
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3L);

        when(productRepository.findById(productDTO.getId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.update(productDTO));
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    public void testChangePriceById_WhenProductExists() {
        
        Long productId = 1L;
        Double newPrice = 99.99;


        when(productRepository.findById(productId)).thenReturn(Optional.of(products.getFirst()));
        when(productRepository.save(products.getFirst())).thenReturn(products.getFirst());

        ProductDTO productDTO = productService.changePriceById(productId, newPrice);

        assertNotNull(productDTO);
        assertEquals(newPrice, products.getFirst().getPrice());
        verify(productRepository, times(1)).save(products.getFirst());
    }

    @Test
    public void testChangePriceById_WhenProductDoesNotExist() {
        
        Long productId = 3L;
        Double newPrice = 99.99;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.changePriceById(productId, newPrice));
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    public void testChangeQuantityById_WhenProductExists() {
        
        Long productId = 1L;
        Integer newQuantity = 50;

        when(productRepository.findById(productId)).thenReturn(Optional.of(products.getFirst()));
        when(productRepository.save(products.getFirst())).thenReturn(products.getFirst());

        ProductDTO productDTO = productService.changeQuantityById(productId, newQuantity);

        assertNotNull(productDTO);
        assertEquals(newQuantity, products.getFirst().getQuantity());
        verify(productRepository, times(1)).save(products.getFirst());
    }

    @Test
    public void testChangeQuantityById_WhenProductDoesNotExist() {
        
        Long productId = 3L;
        Integer newQuantity = 50;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.changeQuantityById(productId, newQuantity));
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    public void testAddProductToCartById_WhenAllValid() {
        
        Long productId = 1L;
        Integer quantity = 2;
        Integer initialQuantity = products.getFirst().getQuantity();
        when(productRepository.findById(productId)).thenReturn(Optional.of(products.getFirst()));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(productRepository.save(products.getFirst())).thenReturn(products.getFirst());
        when(userRepository.save(user)).thenReturn(user);

        ProductDTO addedProductDTO = productService.addProductToCartById(productId, quantity, user);

        assertNotNull(addedProductDTO);
        assertEquals(initialQuantity - quantity, products.getFirst().getQuantity());
        verify(productRepository, times(1)).save(products.getFirst());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAddProductToCartById_WhenProductDoesNotExist() {
        
        Long productId = 3L;
        Integer quantity = 2;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(ProductNotFoundException.class, () -> productService.addProductToCartById(productId, quantity, user));
    }

    @Test
    public void testAddProductToCartById_WhenUserDoesNotExist() {
        
        Long productId = 1L;
        Integer quantity = 2;

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> productService.addProductToCartById(productId, quantity, user));
    }

    @Test
    public void testAddProductToCartById_WhenInsufficientQuantity() {
        
        Long productId = 1L;
        Integer quantity = 200;

        when(productRepository.findById(productId)).thenReturn(Optional.of(products.getFirst()));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(InsufficientQuantityException.class, () -> productService.addProductToCartById(productId, quantity, user));
    }

    @Test
    public void testDelete() {
        
        Long productId = 1L;

        productService.delete(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}