package com.storeManagementTool.StoreManagementTool.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private UserEntity userEntity;

    @OneToMany
    private List<ProductEntity> products = new ArrayList<>();

}
