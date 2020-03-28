package com.example.snapets.controller;

public class StorageController {

    private static StorageController instance = null;

    private StorageController(){


    }

    public static StorageController getInstance(){
        if (instance == null){
            instance = new StorageController();
        }

        return instance;
    }
}
