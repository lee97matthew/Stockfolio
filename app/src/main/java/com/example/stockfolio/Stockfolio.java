package com.example.stockfolio;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.stockfolio.api.StocksApi;

import java.util.ArrayList;
import java.util.List;

public class Stockfolio extends Application {

    private static List<Stock.StockPreview> trendingStocks;
    private static StocksApi stocksApi;
    private static List<Stock.StockPreview> userStocks;

    @Override
    public void onCreate() {
        super.onCreate();
        stocksApi = new StocksApi(getApplicationContext());
        userStocks = new ArrayList<>();
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
        final List<Stock.StockPreview>[] userFavStocks = new List[]{new ArrayList<Stock.StockPreview>()};

        String str = stocks.toString();
        String newStr = str.substring(1,str.length()-1);
        newStr = newStr.replace(" ","");
        stocksApi.getUserQuote(newStr, new StocksApi.GetQuoteUserListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(Stockfolio.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(List<Stock.StockPreview> stocks) {
                userStocks = stocks;
            }
        });

        return userFavStocks[0];
    }

    public List<Stock.StockPreview> getUserStocks() {
        return userStocks;
    }
}
