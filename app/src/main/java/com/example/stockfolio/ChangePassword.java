package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {
    private EditText passwordInput, cfmPasswordInput;
    private Button updatePasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        passwordInput = (EditText) findViewById(R.id.passwordInput);
        cfmPasswordInput = (EditText) findViewById(R.id.cfmPasswordInput);
        updatePasswordButton = (Button) findViewById(R.id.updatePassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://stockfolio-e29ea-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        userId = user.getUid();

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String password = passwordInput.getText().toString().trim();
        String cfmPassword = cfmPasswordInput.getText().toString().trim();


        if (password.isEmpty()) {
            passwordInput.setError("Password is required!");
            passwordInput.requestFocus();
            return;
        } else if (password.length() < 6) {
            passwordInput.setError("Minimum Password length is 6 characters!");
            passwordInput.requestFocus();
            return;
        }

        if (cfmPassword.isEmpty()) {
            cfmPasswordInput.setError("Password Confirmation is required!");
            cfmPasswordInput.requestFocus();
            return;
        } else if (cfmPassword.length() < 6) {
            cfmPasswordInput.setError("Minimum Password length is 6 characters!");
            cfmPasswordInput.requestFocus();
            return;
        }

        if (!password.equals(cfmPassword)) {
            cfmPasswordInput.setError("Password Confirmation is incorrect!");
            cfmPasswordInput.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE); // Show progress bar

        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassword.this, "Password has been successfully changed!", Toast.LENGTH_LONG)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                            ChangePassword.super.onBackPressed();
                        } else {
                            Toast.makeText(ChangePassword.this, "Password could not be changed. Please try again!", Toast.LENGTH_LONG)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

}