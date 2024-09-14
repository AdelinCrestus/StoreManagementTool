package com.storeManagementTool.StoreManagementTool.repositories;

import com.storeManagementTool.StoreManagementTool.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @NonNull
    @Override
    <S extends CartEntity> S save(@NonNull S entity);

    @NonNull
    @Override
    List<CartEntity> findAll();

    @NonNull
    @Override
    Optional<CartEntity> findById(@NonNull Long id);
}
