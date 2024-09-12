package com.storeManagementTool.StoreManagementTool.exceptions;

public class InsufficientQuantityException extends RuntimeException{
    public InsufficientQuantityException(){
        super("Insufficient Quantity");
    }
}
