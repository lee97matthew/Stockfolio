package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Search extends AppCompatActivity {

    Button btn_findStock;
    EditText et_dataInput;
    Stockfolio stockfolio = (Stockfolio) this.getApplication();
    List<Stock.StockPreview> trendingStocks;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // retrieve trending stocks
        trendingStocks = stockfolio.getTrendingStocks();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rv_trendingStocks);
        // use this setting to improve performance
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter
        mAdapter = new TrendingStocksRecycleViewAdapter(trendingStocks, this);
        recyclerView.setAdapter(mAdapter);

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
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        // assign values to each control on the layout
        btn_findStock = findViewById(R.id.btn_findStock);
        et_dataInput = findViewById(R.id.et_dataInput);

        StocksApi stocksApi = new StocksApi(Search.this);

        // click listener for button
        btn_findStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = et_dataInput.getText().toString();

                stocksApi.getQuote(ticker, new StocksApi.GetQuoteListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(Search.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Stock stock) {
                        Toast.makeText(Search.this, String.format("Price of %s: %.2f", ticker, stock.getRegularMarketPrice()), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Search.this, StockPage.class);
                        intent.putExtra("stock", stock);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}