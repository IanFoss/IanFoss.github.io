package com.example.CS499_Capstone

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.edit

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main),
            OnApplyWindowInsetsListener { v: View, insets: WindowInsetsCompat ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })


        val newUserText = findViewById<TextView>(R.id.new_user_text)
        val createUsernameField = findViewById<TextInputLayout>(R.id.create_username_field)
        val createPasswordField = findViewById<TextInputLayout>(R.id.create_password_field)
        val confirmPasswordField = findViewById<TextInputLayout>(R.id.confirm_password_field)
        val createAccountButton = findViewById<Button>(R.id.create_account_button)

        // Shows text fields for new account creation when Create Account button is clicked
        newUserText.setOnClickListener {
            createUsernameField.visibility = View.VISIBLE
            createPasswordField.visibility = View.VISIBLE
            confirmPasswordField.visibility = View.VISIBLE
            createAccountButton.visibility = View.VISIBLE
        }

        val loginButton = findViewById<Button>(R.id.login_button)

        // OnClick listener for log in button manages SMS permissions and navigates to Inventory screen
        loginButton.setOnClickListener {
            val userRepo = UserRepository(this@Login)

            /* -------------------  Account Validation --------------------- */
            val usernameField = findViewById<TextInputLayout>(R.id.username_field)
            val passwordField = findViewById<TextInputLayout>(R.id.password_field)
            val username = usernameField.editText?.text?.toString().orEmpty().trim()
            val password = passwordField.editText?.text?.toString().orEmpty().trim()

            userRepo.validateUser(username, password, { user: UserEntity? ->
                runOnUiThread(
                    Runnable {

                        if (user == null) {
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                            return@Runnable
                        }

                        // If username and password combination exists
                        /* -------------------  SMS Permissions --------------------- */

                        val preferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        val askedBefore =
                            preferences.getBoolean("asked_sms_permission", false)

                        // Permissions are not granted for SMS
                        if (ContextCompat.checkSelfPermission(
                                this@Login,
                                Manifest.permission.SEND_SMS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // User has denied permission once

                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this@Login,
                                    Manifest.permission.SEND_SMS
                                )
                            ) {
                                AlertDialog.Builder(this@Login)
                                    .setTitle("SMS Permission Required")
                                    .setMessage("We need SMS permission to send you alerts.")
                                    .setPositiveButton(
                                        "Okay",
                                        DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                                            ActivityCompat.requestPermissions(
                                                this@Login, arrayOf(
                                                    Manifest.permission.SEND_SMS
                                                ), 1001
                                            )
                                        })

                                    .show()
                            } else if (!askedBefore) {
                                preferences.edit {
                                    putBoolean("asked_sms_permission", true)
                                }
                                AlertDialog.Builder(this@Login)
                                    .setTitle("SMS Permission Required")
                                    .setMessage("This app needs permission to send SMS messages for alerts.")
                                    .setPositiveButton(
                                        "Okay",
                                        DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                                            ActivityCompat.requestPermissions(
                                                this@Login, arrayOf(
                                                    Manifest.permission.SEND_SMS
                                                ), 1001
                                            )
                                        })

                                    .show()
                            } else {
                                Toast.makeText(
                                    this@Login,
                                    "Please enable SMS permissions in app settings to receive text alerts.",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent =
                                    Intent(this@Login, InventoryActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            val intent = Intent(this@Login, InventoryActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            })
        }


        // TextWatcher shows real-time error message if passwords don't match
        confirmPasswordField.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val password = createPasswordField.editText?.text?.toString().orEmpty().trim()
                val confirmPassword = s.toString()

                if (confirmPassword != password) {
                    confirmPasswordField.setError("Passwords do not match.")
                } else {
                    confirmPasswordField.setError(null)
                }
            }
        })

        // Inserts new user entity into database after successful validation of credentials
        createAccountButton.setOnClickListener {
            if (validateInputs()) {
                val repo = UserRepository(this@Login)
                val newUsername = createUsernameField.editText?.text?.toString().orEmpty().trim()
                val newPassword = createPasswordField.editText?.text?.toString().orEmpty().trim()

                // Uses repo method to ensure duplicate username is not created
                repo.usernameExists(newUsername,  { exists ->
                    runOnUiThread {
                        if (exists) {
                            createUsernameField.error = "Username already exists."
                            return@runOnUiThread
                        }

                        val newUser = UserEntity(username = newUsername, password = newPassword)

                        repo.insert(newUser)
                        Toast.makeText(this@Login, "Account Created", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }


    // Callback for SMS permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001) {
            // Navigates to inventory screen if permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this@Login, InventoryActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "SMS permission denied. You won't receive text alerts.",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this@Login, InventoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // Helper function that validates new password meets requirements
    private fun validatePasswordStrength(password: String): String? {

        if (password.length < 8) {
            return "Password must be at least 8 characters."
        }

        if (!password.any { it.isUpperCase() }) {
            return "Password must contain at least one uppercase letter."
        }

        if (!password.any { it.isLowerCase() }) {
            return "Password must contain at least one lowercase letter."
        }

        if (!password.any { it.isDigit() }) {
            return "Password must contain at least one number."
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            return "Password must contain at least one special character."
        }

        return null
    }


    // Helper function to validate input for create account button listener
    private fun validateInputs(): Boolean {
        val usernameField = findViewById<TextInputLayout>(R.id.create_username_field)
        val passwordField = findViewById<TextInputLayout>(R.id.create_password_field)
        val confirmPasswordField = findViewById<TextInputLayout>(R.id.confirm_password_field)

        var isValid = true

        // Validates username is not blank
        val username = usernameField.editText?.text?.toString().orEmpty().trim()
        if (username.isEmpty()) {
            usernameField.setError("Username cannot be empty.")
            isValid = false
        } else {
            usernameField.setError(null)
        }

        // Validates password is not blank
        val password = passwordField.editText?.text?.toString().orEmpty().trim()
        if (password.isEmpty()) {
            passwordField.setError("Password cannot be empty.")
            isValid = false
        } else {

            val passwordError = validatePasswordStrength(password)
            if (passwordError != null) {
                passwordField.error = passwordError
                isValid = false
            }
            else {
                passwordField.setError(null)
            }
        }

        val confirmPassword =
            confirmPasswordField.editText?.text?.toString().orEmpty().trim()
        if (password != confirmPassword) {
            confirmPasswordField.setError("Passwords do not match.")
            isValid = false
        } else {
            confirmPasswordField.setError(null)
        }
        return isValid
    }
}