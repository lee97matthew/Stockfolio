package com.example.stockfolio.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stockfolio.RequestSingleton;
import com.example.stockfolio.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StocksApi {

    public static final String QUERY = "https://yh-finance.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=";
    public static final String QUERY_TRENDINGTICKERS = "https://yh-finance.p.rapidapi.com/market/get-trending-tickers?region=US";
    private static final String X_RAPIDAPI_HOST_VALUE = "yh-finance.p.rapidapi.com";
    private static final String X_RAPIDAPI_KEY_VALUE = "c0302ed34bmsh4ae1b7b7eb0514bp1ff5adjsn4d70320ec3d7";

    Context context;
    double currentMarketPrice;

    public StocksApi(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(Stock stock);
    }

    public interface GetTrendingTickersListener {
        void onError(String message);

        void onResponse(List<Stock> stocks);
    }

    public void retrieveTickerSymbol(String ticker, VolleyResponseListener volleyResponseListener) {
        String url = QUERY + ticker;

        // Get JSON Object (curly braces {}) from API, JSON Array (square brackets [])
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObjectStock;
                        Stock stock;
                        try {
                            jsonObjectStock = response.getJSONObject("quoteResponse").getJSONArray("result").getJSONObject(0);
                            stock = new Stock(jsonObjectStock);
                            volleyResponseListener.onResponse(stock);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Error!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-RapidAPI-Host", X_RAPIDAPI_HOST_VALUE);
                params.put("X-RapidAPI-Key", X_RAPIDAPI_KEY_VALUE);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getTrendingTickers(GetTrendingTickersListener getTrendingTickersListener) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, QUERY_TRENDINGTICKERS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArrayStocks;
                        List<Stock> stocks = new ArrayList<>();
                        try {
                            jsonArrayStocks = response.getJSONObject("finance").getJSONArray("result").getJSONObject(0).getJSONArray("quotes");
                            for (int i = 0; i < jsonArrayStocks.length(); i++) {
                                JSONObject stock = jsonArrayStocks.getJSONObject(i);
                                stocks.add(new Stock(stock));
                            }
                            getTrendingTickersListener.onResponse(stocks);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getTrendingTickersListener.onError("Error!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-RapidAPI-Host", X_RAPIDAPI_HOST_VALUE);
                params.put("X-RapidAPI-Key", X_RAPIDAPI_KEY_VALUE);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
