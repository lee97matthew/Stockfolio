package com.example.stockfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StockPage extends AppCompatActivity {

    private TextView text_stockName, text_regularMarketPrice;
    private Button btn_fav;
    Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);

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

        // retrieve Stock object from previous Activity
        Intent intent = getIntent();
        stock = (Stock) intent.getParcelableExtra("stock");

        // Update local views with information of Stock
        Toast.makeText(this, "ticker: " + stock.getSymbol(), Toast.LENGTH_LONG).show();
        text_stockName.setText(stock.getSymbol());
        text_regularMarketPrice.setText(String.valueOf(stock.getRegularMarketPrice()));
    }
}