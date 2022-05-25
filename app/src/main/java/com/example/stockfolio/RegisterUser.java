package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private EditText editFullName, editEmail, editPassword, editCfmPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner); // click on banner, go back to home page
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser); // click on button to register account
        registerUser.setOnClickListener(this);

        editFullName = (EditText) findViewById(R.id.fullName);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        editCfmPassword = (EditText) findViewById(R.id.confirmPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String fullName = editFullName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String cfmPassword = editCfmPassword.getText().toString().trim();

        // Input Validation
        if (fullName.isEmpty()) {
            editFullName.setError("Full Name is required!");
            editFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editEmail.setError("Email is required!");
            editEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Email is invalid!");
            editEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            editPassword.setError("Minimum Password length is 6 characters!");
            editPassword.requestFocus();
            return;
        }

        if (cfmPassword.isEmpty()) {
            editCfmPassword.setError("Password Confirmation is required!");
            editCfmPassword.requestFocus();
            return;
        } else if (cfmPassword.length() < 6) {
            editCfmPassword.setError("Minimum Password length is 6 characters!");
            editCfmPassword.requestFocus();
            return;
        }

        if (!password.equals(cfmPassword)) {
            editCfmPassword.setError("Password Confirmation is incorrect!");
            editCfmPassword.requestFocus();
            return;
        }
        // End of Input Validation

        progressBar.setVisibility(View.VISIBLE); // Show progress bar

        // Use Firebase to create and save new account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // User created successfully by Firebase
                            User newUser = new User(fullName, email); // Create Java user class instance
                            // TODO: put in the stockslist, firebase get data, use the user as ref

                            // Instantiate Firebase Realtime Db Connection
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://stockfolio-e29ea-default-rtdb.asia-southeast1.firebasedatabase.app");

                            // Persist new User data
                            database.getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(newUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) { // User data properly persisted
                                                // Push Notification
                                                Toast.makeText(RegisterUser.this, "User has been successfully registered!", Toast.LENGTH_LONG)
                                                        .show();
                                                progressBar.setVisibility(View.GONE);

                                                // Redirect User
                                            } else { // User data not persisted properly
                                                Toast.makeText(RegisterUser.this, "User has not been successfully registered. Please try again!", Toast.LENGTH_LONG)
                                                        .show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                        } else { // User not successfully created by Firebase
                            Toast.makeText(RegisterUser.this, "User has not been successfully registered. Please try again!", Toast.LENGTH_LONG)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}