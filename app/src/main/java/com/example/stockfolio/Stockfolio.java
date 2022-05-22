package com.example.stockfolio;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;

import java.util.List;

public class Stockfolio extends Application {

    private static List<Stock> trendingStocks;
    private static StocksApi stocksApi;

    @Override
    public void onCreate() {
        super.onCreate();
        stocksApi = new StocksApi(getApplicationContext());
        fillTrendingStocks();
    }

    // retrieve the trending stocks for today
    public void fillTrendingStocks() {
        stocksApi.getTrendingTickers(new StocksApi.GetTrendingTickersListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(Stockfolio.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(List<Stock> stocks) {
                trendingStocks = stocks;
                Toast.makeText(Stockfolio.this, "success! " + stocks.get(0), Toast.LENGTH_LONG).show();
            }
        });
    }
}
