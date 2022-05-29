package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class StockPage extends AppCompatActivity {

    private TextView text_stockName, text_regularMarketPrice;
    private Button btn_fav;
    Stock stock;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);
        Log.d("here2","enter StockPage");

        // Initialize nav bar
        BottomNavigationView botNavView = findViewById(R.id.bottomNavigation);

        // Set current selected item
        botNavView.setSelectedItemId(R.id.stocks);

        // Enable switching of page
        botNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
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

        // instantiate local views
        text_stockName = (TextView) findViewById(R.id.text_stockName);
        text_regularMarketPrice = (EditText) findViewById(R.id.text_regularMarketPrice);
        btn_fav = (Button) findViewById(R.id.btn_fav);

        // register the onClick listener
        btn_fav.setOnClickListener(favListener);

        // retrieve Stock object from previous Activity
        Intent intent = getIntent();
        stock = (Stock) intent.getParcelableExtra("stock");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://stockfolio-e29ea-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    // check if userProfile.favStocks contains stock
                    if (userProfile.favoritedStocks.contains(stock.getSymbol())) {
                        // if contain, set button to favorited
                        btn_fav.setText("Unfavorite");
                    } else {
                        // if not contain, set button as not-favorited
                        btn_fav.setText("Add to Favorite");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StockPage.this, "GG what did you do?", Toast.LENGTH_LONG)
                        .show();
            }
        });

        // Update local views with information of Stock
        text_stockName.setText(stock.getSymbol());
        text_regularMarketPrice.setText(String.valueOf(stock.getRegularMarketPrice()));
    }

    // make button click change the text view
    private View.OnClickListener favListener = new View.OnClickListener() {
        public void onClick(View v) {
            List<String> stocks = new ArrayList<>();

            if (btn_fav.getText().equals("Unfavorite")) {
                // unfavorite
                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);

                        if (userProfile != null) {
                            stocks.addAll(userProfile.getFavoritedStocks());
                        }

                        Toast.makeText(StockPage.this, "Favs retrieved, " + stocks.toString(), Toast.LENGTH_LONG)
                                .show();

                        stocks.remove(stock.getSymbol());

                        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> postValues = new HashMap<String,Object>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    postValues.put(snapshot.getKey(),snapshot.getValue());
                                }
                                postValues.put("favoritedStocks", stocks);
                                reference.child(userId).updateChildren(postValues)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(StockPage.this, "Successfully updated!", Toast.LENGTH_LONG)
                                                            .show();
                                                    btn_fav.setText("Add to Favorite");
                                                } else {
                                                    Toast.makeText(StockPage.this, "Not successfully updated!", Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(StockPage.this, "Not successfully updated!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StockPage.this, "GG what did you do?", Toast.LENGTH_LONG)
                                .show();
                    }
                });

            } else {
                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);

                        if (userProfile != null) {
                            stocks.addAll(userProfile.getFavoritedStocks());
                        }

                        Toast.makeText(StockPage.this, "Favs retrieved, " + stocks.toString(), Toast.LENGTH_LONG)
                                .show();

                        stocks.add(stock.getSymbol());

                        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> postValues = new HashMap<String,Object>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    postValues.put(snapshot.getKey(),snapshot.getValue());
                                }
                                postValues.put("favoritedStocks", stocks);
                                reference.child(userId).updateChildren(postValues)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(StockPage.this, "Successfully updated!", Toast.LENGTH_LONG)
                                                            .show();
                                                    btn_fav.setText("Unfavorite");
                                                } else {
                                                    Toast.makeText(StockPage.this, "Not successfully updated!", Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(StockPage.this, "Not successfully updated!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StockPage.this, "GG what did you do?", Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

        }

    };

}