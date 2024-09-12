package com.storeManagementTool.StoreManagementTool.repositories;

import com.storeManagementTool.StoreManagementTool.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @NonNull
    @Override
    Optional<ProductEntity> findById(@NonNull Long id);

    List<ProductEntity> findProductEntitiesByDescriptionContainingIgnoreCase(@NonNull String description);
    List<ProductEntity> findProductEntitiesByNameContainingIgnoreCase(@NonNull String name);
    List<ProductEntity> findProductEntitiesByDescriptionContainingIgnoreCaseAndNameContainingIgnoreCase(String description, String name);


    @NonNull
    @Override
    List<ProductEntity> findAll();

    @NonNull
    @Override
    <S extends ProductEntity> S save(@NonNull S entity);

    @NonNull
    @Override
    <S extends ProductEntity> List<S> saveAll(@NonNull Iterable<S> entities);

    @Override
    void deleteById(@NonNull Long id);


}
