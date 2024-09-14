package com.storeManagementTool.StoreManagementTool.mappers;

import com.storeManagementTool.StoreManagementTool.dtos.CartDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.CartEntity;
import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public  interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    ProductEntity dtoToEntity(ProductAddDTO productaddDTO);
    ProductDTO entityToDto(ProductEntity productEntity);

    @Mapping(target = "totalPrice", source = ".", qualifiedByName = "setTotalPrice")
    CartDTO entityToDto(CartEntity cartEntity);

    @Named("setTotalPrice")
    default Double setTotalPrice(CartEntity cartEntity) {
        return cartEntity.getProducts().stream().mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }

}
