package com.example.stockfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;

public class Home extends AppCompatActivity {

    Button btn_findStock;
    EditText et_dataInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // assign values to each control on the layout
        btn_findStock = findViewById(R.id.btn_findStock);
        et_dataInput = findViewById(R.id.et_dataInput);

        StocksApi stocksApi = new StocksApi(Home.this);

        // click listener for button
        btn_findStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = et_dataInput.getText().toString();

                stocksApi.getMarketPrice(ticker, new StocksApi.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(Home.this, "Something wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(double currentMarketPrice) {
                        Toast.makeText(Home.this, String.format("Price of %s: %.2f", ticker, currentMarketPrice), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}