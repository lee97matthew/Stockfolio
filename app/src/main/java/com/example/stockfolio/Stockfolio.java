package com.example.stockfolio;

import android.app.Application;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Stockfolio extends Application {

    private static List<Stock.StockPreview> trendingStocks;
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
            public void onResponse(List<Stock.StockPreview> stocks) {
                trendingStocks = stocks;
            }
        });
    }

    public static List<Stock.StockPreview> getTrendingStocks() {
        return trendingStocks;
    }

    public static void setTrendingStocks(List<Stock.StockPreview> trendingStocks) {
        Stockfolio.trendingStocks = trendingStocks;
    }

    public List<Stock.StockPreview> getFavStocks(List<String> stocks) {
        List<Stock.StockPreview> userFavStocks = new ArrayList<>();

        for (String s : stocks) {
            stocksApi.getUserQuote(s, new StocksApi.GetQuoteUserListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(Stockfolio.this, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Stock.StockPreview stock) {
                    userFavStocks.add(stock);
                }
            });

        }

        return userFavStocks;
    }
}
