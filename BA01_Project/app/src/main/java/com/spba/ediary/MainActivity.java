package com.spba.ediary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.spba.ediary.controllers.DatabaseController;
import com.spba.ediary.views.LoginActivity;
import com.spba.ediary.views.RegisterActivity;

public class MainActivity extends AppCompatActivity {


    private Button logIn;
    private Button register;
    private DatabaseController databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseController = new DatabaseController(this);

        //loadTestData();

        register = findViewById(R.id.registerButton);
        logIn = findViewById(R.id.login);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterPage();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogInPage();
            }
        });

    }

    public void openRegisterPage(){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }


    private void openLogInPage() {
        Intent logInIntent = new Intent(this, LoginActivity.class);
        startActivity(logInIntent);
    }


    /**
     * Initializes test data only once if no data is in the database
     */
    private void loadTestData(){

        if(databaseController.checkForTestData()){
            new TestDataInitialisation(this).initialiseData();
        }

    }
}