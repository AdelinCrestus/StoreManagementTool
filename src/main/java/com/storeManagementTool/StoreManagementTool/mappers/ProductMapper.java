package com.storeManagementTool.StoreManagementTool.mappers;

import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public  interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    ProductEntity dtoToEntity(ProductAddDTO productaddDTO);
    ProductDTO entityToDto(ProductEntity productEntity);

}
