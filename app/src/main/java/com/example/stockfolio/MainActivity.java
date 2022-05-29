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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgetPassword;
    private EditText editEmail, editPassword;
    private Button login;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    Stockfolio stockfolio = (Stockfolio) this.getApplication();
//    stockfolio.fillTrendingStocks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgetPassword:
                startActivity(new Intent(this, ForgetPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Email and Password Validation
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
        // End of Input Validation

        progressBar.setVisibility(View.VISIBLE); // Show progress bar

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // Login is successful
                            // Check if email is verified
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()) { // User verified
                                // Redirect to profile page
                                //startActivity(new Intent(MainActivity.this, UserProfile.class));

                                // Redirect to Dashboard page
                                startActivity(new Intent(MainActivity.this, Dashboard.class));
                            } else { // User not verified
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Please verify account through the email sent to you.", Toast.LENGTH_LONG)
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            }

                        } else { // Login unsuccessful
                            Toast.makeText(MainActivity.this, "Failed to login. Please try again!", Toast.LENGTH_LONG)
                                .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}