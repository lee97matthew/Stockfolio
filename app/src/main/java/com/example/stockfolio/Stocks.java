package com.example.stockfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;

public class Stocks extends AppCompatActivity {

    Button btn_findStock;
    EditText et_dataInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        // assign values to each control on the layout
        btn_findStock = findViewById(R.id.btn_findStock);
        et_dataInput = findViewById(R.id.et_dataInput);

        StocksApi stocksApi = new StocksApi(Stocks.this);

        // click listener for button
        btn_findStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = et_dataInput.getText().toString();

                stocksApi.getMarketPrice(ticker, new StocksApi.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(Stocks.this, "Something wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(double currentMarketPrice) {
                        Toast.makeText(Stocks.this, String.format("Price of %s: %.2f", ticker, currentMarketPrice), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}