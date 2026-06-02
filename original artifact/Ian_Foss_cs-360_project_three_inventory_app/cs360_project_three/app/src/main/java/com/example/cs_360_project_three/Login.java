package com.example.cs_360_project_three;

import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;



public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TextView newUserText = findViewById(R.id.new_user_text);
        TextInputLayout createUsernameField = findViewById(R.id.create_username_field);
        TextInputLayout createPasswordField = findViewById(R.id.create_password_field);
        TextInputLayout confirmPasswordField = findViewById(R.id.confirm_password_field);
        Button createAccountButton = findViewById(R.id.create_account_button);

        // Shows text fields for new account creation when Create Account button is clicked
        newUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUsernameField.setVisibility(VISIBLE);
                createPasswordField.setVisibility(VISIBLE);
                confirmPasswordField.setVisibility(VISIBLE);
                createAccountButton.setVisibility(VISIBLE);
            }
        });

        Button loginButton = findViewById(R.id.login_button);

        // OnClick listener for log in button manages SMS permissions and navigates to Inventory screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRepository userRepo = new UserRepository(Login.this);

                /* -------------------  Account Validation --------------------- */

                TextInputLayout usernameField = findViewById(R.id.username_field);
                TextInputLayout passwordField = findViewById(R.id.password_field);
                String username = usernameField.getEditText().getText().toString().trim();
                String password = passwordField.getEditText().getText().toString().trim();

                userRepo.validateUser(username, password, user -> {
                    runOnUiThread(() -> {

                        // If username and password combination exists
                        if (user != null) {

                            /* -------------------  SMS Permissions --------------------- */

                            SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            boolean askedBefore = preferences.getBoolean("asked_sms_permission", false);

                            // Permissions are not granted for SMS
                            if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                                // User has denied permission once
                                if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, Manifest.permission.SEND_SMS)) {
                                    new AlertDialog.Builder(Login.this)
                                            .setTitle("SMS Permission Required")
                                            .setMessage("We need SMS permission to send you alerts.")
                                            .setPositiveButton("Okay", (dialog, which) -> {
                                                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.SEND_SMS}, 1001);
                                            })

                                            .show();
                                }
                                // First time asking user for permission
                                else if (!askedBefore) {
                                    preferences.edit().putBoolean("asked_sms_permission", true).apply();
                                    new AlertDialog.Builder(Login.this)
                                            .setTitle("SMS Permission Required")
                                            .setMessage("This app needs permission to send SMS messages for alerts.")
                                            .setPositiveButton("Okay", (dialog, which) -> {
                                                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.SEND_SMS}, 1001);
                                            })

                                            .show();
                                }
                                // User permanently denied permission
                                // Asks user to enable permission in settings
                                else {
                                    Toast.makeText(Login.this, "Please enable SMS permissions in app settings to receive text alerts.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this, InventoryActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            // Permissions are allowed for SMS
                            else {
                                Intent intent = new Intent(Login.this, InventoryActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        // Prompts user to create an account if no match to credentials
                        else {
                            Toast.makeText(Login.this, "Invalid username or password. \nPlease create an account.", Toast.LENGTH_LONG).show();

                        }
                    });
                });
            }
        });


        // Textwatcher shows real-time error message if passwords don't match
        confirmPasswordField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String password = createPasswordField.getEditText().getText().toString();
                String confirmPassword = s.toString();

                if (!confirmPassword.equals(password)) {
                    confirmPasswordField.setError("Passwords do not match.");
                }
                else {
                    confirmPasswordField.setError(null);
                }
            }
        });

        // Inserts new user entity into database after successful validation of credentials
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (validateInputs()) {

                    UserRepository repo = new UserRepository(Login.this);
                    String newUsername = createUsernameField.getEditText().getText().toString();
                    String newPassword = createPasswordField.getEditText().getText().toString();

                    UserEntity newUser = new UserEntity();

                    newUser.setUsername(newUsername);
                    newUser.setPassword(newPassword);

                    repo.insert(newUser);
                    Toast.makeText(Login.this, "Account Created", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    // Callback for SMS permission request
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            // Navigates to inventory screen if permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Login.this, InventoryActivity.class);
                startActivity(intent);
                finish();
            }
            // Displays toast if permission denied, then navigates to inventory screen
            else {
                Toast.makeText(this, "SMS permission denied. You won't receive text alerts.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, InventoryActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    // Helper function to validate input for create account button listener
    private boolean validateInputs() {

        TextInputLayout usernameField = findViewById(R.id.create_username_field);
        TextInputLayout passwordField = findViewById(R.id.create_password_field);
        TextInputLayout confirmPasswordField = findViewById(R.id.confirm_password_field);

        boolean isValid = true;

        // Validates username is not blank
        String username = usernameField.getEditText().getText().toString().trim();
        if (username.isEmpty()) {
            usernameField.setError("Username cannot be empty.");
            isValid = false;
        }
        else {
            usernameField.setError(null);
        }

        // Validates password is not blank
        String password = passwordField.getEditText().getText().toString().trim();
        if(password.isEmpty()) {
            passwordField.setError("Password cannot be empty.");
            isValid = false;
        }
        else {
            passwordField.setError(null);
        }

        String confirmPassword = confirmPasswordField.getEditText().getText().toString().trim();
        if(!password.equals(confirmPassword)) {
            confirmPasswordField.setError("Passwords do not match.");
            isValid = false;
        }
        else {
            confirmPasswordField.setError(null);
        }
        return isValid;
    }
}