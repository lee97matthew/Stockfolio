package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private Button userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize nav bar
        BottomNavigationView botNavView = findViewById(R.id.bottomNavigation);

        // Set current selected item
        botNavView.setSelectedItemId(R.id.dashboard);

        // Enable switching of page
        botNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        return true;
                    case R.id.stocks:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        userProfile = (Button) findViewById(R.id.goToProfile);
        userProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goToProfile:
                addTicker();
                break;
        }
    }

    private void addTicker() {
        FirebaseUser user;
        DatabaseReference reference;
        String userId;
        List<String> tickers = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://stockfolio-e29ea-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    tickers.addAll(userProfile.getStocks());
                }

                Toast.makeText(Dashboard.this, "Favs retrieved, " + tickers.toString(), Toast.LENGTH_LONG)
                        .show();

                tickers.add("AMD");

                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> postValues = new HashMap<String,Object>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postValues.put(snapshot.getKey(),snapshot.getValue());
                        }
                        postValues.put("favTickers", tickers);
                        reference.child(userId).updateChildren(postValues)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Dashboard.this, "Successfully updated!", Toast.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            Toast.makeText(Dashboard.this, "Not successfully updated!", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Dashboard.this, "Not successfully updated!", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "GG what did you do?", Toast.LENGTH_LONG)
                        .show();
            }
        });

    }
}