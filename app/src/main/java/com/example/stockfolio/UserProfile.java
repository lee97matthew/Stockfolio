package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    private Button logout, changePassword;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize nav bar
        BottomNavigationView botNavView = findViewById(R.id.bottomNavigation);

        // Set current selected item
        botNavView.setSelectedItemId(R.id.profile);

        // Enable switching of page
        botNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
//                    case R.id.stocks: // disabled for now
//                        startActivity(new Intent(getApplicationContext(), StockPage.class));
//                        overridePendingTransition(0, 0);
//                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        changePassword = (Button) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://stockfolio-e29ea-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        userId = user.getUid();

        final TextView userFullNameTextView = (TextView) findViewById(R.id.userFullName);
        final TextView userEmailTextView = (TextView) findViewById(R.id.userEmail);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    userFullNameTextView.setText(userProfile.fullName);
                    userEmailTextView.setText(userProfile.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "GG what did you do?", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                userLogout();
                break;
            case R.id.changePassword:
                startActivity(new Intent(this, ChangePassword.class));
                break;
        }
    }

    private void userLogout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(UserProfile.this, "Logging Out.", Toast.LENGTH_LONG)
                        .show();
                startActivity(new Intent(UserProfile.this, MainActivity.class));
            }
        });
    }
}