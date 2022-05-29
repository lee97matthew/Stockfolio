package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements FavoritedStocksRecycleViewAdapter.FavoritedStockListener {
    Stockfolio stockfolio;
    List<Stock.StockPreview> favStocks;
    StocksApi stocksApi;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://stockfolio-e29ea-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
        String userId = user.getUid();
        stockfolio = (Stockfolio) this.getApplication();
        favStocks = new ArrayList<Stock.StockPreview>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE); // Show progress bar
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    // retrieve user's fav stocks
                    List<String> stocks = userProfile.getFavoritedStocks();
                    stocks.remove("test");
                    // retrieve fav stocks data
                    favStocks = stockfolio.getFavStocks(stocks);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            favStocks = stockfolio.getUserStocks();
                        }
                    }, 3500); // 3.5 seconds
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "GG what did you do?", Toast.LENGTH_LONG)
                        .show();
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rv_favoritedStocks);
        // use this setting to improve performance
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter

        new Handler().postDelayed(new Runnable() {
            public void run() {
                setRecycleViewData();
                progressBar.setVisibility(View.GONE);
            }
        }, 5000); // 5 seconds

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

    }

    @Override
    public void favoritedStockClick(int position) {
        Stock.StockPreview favStockSelected = favStocks.get(position);
        String trendingStockTicker = favStockSelected.getSymbol();
        stocksApi = new StocksApi(Dashboard.this);
        stocksApi.getQuote(trendingStockTicker, new StocksApi.GetQuoteListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(Dashboard.this, "smth happened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Stock stock) {
                Intent intent = new Intent(Dashboard.this, StockPage.class);
                intent.putExtra("stock", stock);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    public void setRecycleViewData() {
        mAdapter = new FavoritedStocksRecycleViewAdapter(favStocks, this, this);
        recyclerView.setAdapter(mAdapter);
    }
}