package com.storeManagementTool.StoreManagementTool.mappers;

import com.storeManagementTool.StoreManagementTool.dtos.ProductAddDTO;
import com.storeManagementTool.StoreManagementTool.dtos.ProductDTO;
import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity dtoToEntity(ProductAddDTO productaddDTO);
    ProductDTO entityToDto(ProductEntity productEntity);

}
