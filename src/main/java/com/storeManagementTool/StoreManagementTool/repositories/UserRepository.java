package com.storeManagementTool.StoreManagementTool.repositories;

import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
