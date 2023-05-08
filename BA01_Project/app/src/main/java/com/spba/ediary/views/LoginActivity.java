package com.spba.ediary.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spba.ediary.R;
import com.spba.ediary.controllers.UserController;
import com.spba.ediary.exceptions.NoUserRegisteredException;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button fingerprintButton;
    private EditText password;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    ConstraintLayout constraintLayout;

    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_button);
        fingerprintButton = findViewById(R.id.fingerprint_login);
        password = (EditText) findViewById(R.id.login_password);

        userController = new UserController(this);

        try {
            userController.checkIfUserExists();
            login.setEnabled(true);
            fingerprintButton.setEnabled(true);

            setLoginWithPassword();
            checkBiometricSupport();
            setFingerprintProcess();
        } catch (NoUserRegisteredException e) {
            login.setEnabled(false);
            fingerprintButton.setEnabled(false);
            Toast.makeText(LoginActivity.this, "Please Register a User.", Toast.LENGTH_SHORT).show();

        }




    }

    /**
     * Manages the biometric login option.
     */

    private void setFingerprintProcess() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, "Authentication Error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginActivity.this, "Authentication succeeded.", Toast.LENGTH_SHORT).show();

                Intent goToMainPageIntent = new Intent(LoginActivity.this, DisplayPageActivity.class);
                startActivity(goToMainPageIntent);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

        fingerprintButton.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promptInfo =  dialogMetric();
            promptInfo.setNegativeButtonText("Cancel");
            biometricPrompt.authenticate(promptInfo.build());
        });
    }


    /**
     *
     * sets the login onClickListener and checks the correctness of the password
     */

    private void setLoginWithPassword() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (userController.checkPassword(password.getText().toString())) {
                        Intent goToMainPageIntent = new Intent(LoginActivity.this, DisplayPageActivity.class);
                        startActivity(goToMainPageIntent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter the correct password!",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NoUserRegisteredException e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    BiometricPrompt.PromptInfo.Builder dialogMetric() {

        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("Login using your fingerprint");
    }


    private void checkBiometricSupport() {
        String infoMessage = "";
        BiometricManager biometricManager = BiometricManager.from(LoginActivity.this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                infoMessage = "No biometric feature available on this device!";
                enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_SUCCESS:
                infoMessage = "App can authenticate biometric!";
                enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                infoMessage = "No finger registered. Please register one!";
                enableButton(false, true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                infoMessage = "Biometric feature are not available!";
                enableButton(false);
                break;
            default:
                infoMessage = "Unknown cause.";
                enableButton(false);
                break;

        }
        Toast.makeText(LoginActivity.this, infoMessage,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Enable and disables the login button if the user has registered and account
     * @param enable
     * @param enroll
     */
    private void enableButton(boolean enable, boolean enroll) {
        enableButton(enable);
        if (!enroll) return;
        Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK);
        startActivity(enrollIntent);
    }

    private void enableButton(boolean enable) {
        fingerprintButton.setEnabled(enable);
    }
}