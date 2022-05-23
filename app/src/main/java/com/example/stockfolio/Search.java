package com.example.stockfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;

import java.util.List;

public class Search extends AppCompatActivity {

    Button btn_findStock;
    EditText et_dataInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // assign values to each control on the layout
        btn_findStock = findViewById(R.id.btn_findStock);
        et_dataInput = findViewById(R.id.et_dataInput);

        StocksApi stocksApi = new StocksApi(Search.this);

        // click listener for button
//        btn_findStock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String ticker = et_dataInput.getText().toString();
//
//                stocksApi.getQuote(ticker, new StocksApi.GetQuoteListener() {
//                    @Override
//                    public void onError(String message) {
//                        Toast.makeText(Search.this, message, Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onResponse(Stock stock) {
//                        Toast.makeText(Search.this, String.format("Price of %s: %.2f", ticker, stock.getRegularMarketPrice()), Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Search.this, StockPage.class);
//                        intent.putExtra("stock", stock);
//                        startActivity(intent);
//                    }
//                });
//            }
//        });

        // click listener for button
        btn_findStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = et_dataInput.getText().toString();

                stocksApi.getAutoComplete(ticker, new StocksApi.GetAutoCompleteListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(Search.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(List<Stock> suggestedStocks) {
                        Toast.makeText(Search.this, "getAutoComplete | success!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}