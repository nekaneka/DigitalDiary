package com.spba.ediary.controllers;

import android.content.Context;

import com.spba.ediary.exceptions.NoUserRegisteredException;

public class UserController {

    Context context;
    private DatabaseController databaseController;

    public UserController(Context context) {
        this.context = context;
        this.databaseController = DatabaseController.getDatabaseController();
    }

    public void checkIfUserExists() throws NoUserRegisteredException {
        databaseController.getUser();
    }

    public boolean checkPassword(String password) throws NoUserRegisteredException {
        return databaseController.getUser().getPassword().equals(password);
    }

    public void registerNewUser(String name, String password) {
        databaseController.registerNewUser(name, password);
    }
}
