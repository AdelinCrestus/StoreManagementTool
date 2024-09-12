package com.storeManagementTool.StoreManagementTool.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("Product Not Found");
    }
}
