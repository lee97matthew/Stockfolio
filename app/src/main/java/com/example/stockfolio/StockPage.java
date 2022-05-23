package com.example.stockfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StockPage extends AppCompatActivity {

    private TextView text_stockName, text_regularMarketPrice;
    private Button btn_fav;
    Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);

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