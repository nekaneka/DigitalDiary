package com.spba.ediary.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spba.ediary.R;
import com.spba.ediary.controllers.DatabaseController;
import com.spba.ediary.controllers.UserController;
import com.spba.ediary.exceptions.NoUserRegisteredException;

/**
 * Registers a user account with username and password
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText accountUserName;
    private EditText password;
    private EditText passwordRepeat;
    private Button registerAccountButton;

    private UserController userController;
    private DatabaseController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        accountUserName = findViewById(R.id.nameInput);
        password = findViewById(R.id.register_password);
        passwordRepeat = findViewById(R.id.repeat_register_password);
        registerAccountButton = findViewById(R.id.registerSubmitButton);

        userController = new UserController(this);
        dbController = DatabaseController.getDatabaseController();


        registerAccountButton.setOnClickListener(view -> {
            if (password.getText().toString().equals(passwordRepeat.getText().toString())) {
                try {
                        userController.checkIfUserExists();
                        Toast.makeText(RegisterActivity.this, "User Already Exists!",
                                Toast.LENGTH_SHORT).show();

                } catch (NoUserRegisteredException e) {
                    userController.registerNewUser(accountUserName.getText().toString(), password.getText().toString());
                    Toast.makeText(RegisterActivity.this, accountUserName.getText() + ": " + password.getText(),
                            Toast.LENGTH_SHORT).show();

                    finish();                }
            } else
                Toast.makeText(RegisterActivity.this, "Please check passwords again",
                        Toast.LENGTH_SHORT).show();
        });
    }
}