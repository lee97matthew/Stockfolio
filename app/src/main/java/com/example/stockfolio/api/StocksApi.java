package com.example.stockfolio.api;

import android.content.Context;
import android.util.Log;

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

    public static final String QUERY_QUOTE = "https://yh-finance.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=";
    public static final String QUERY_TRENDINGTICKERS = "https://yh-finance.p.rapidapi.com/market/get-trending-tickers?region=US";
    public static final String QUERY_AUTOCOMPLETE = "https://yh-finance.p.rapidapi.com/auto-complete?q=%s&region=US";
    private static final String X_RAPIDAPI_HOST_VALUE = "yh-finance.p.rapidapi.com";
    private static final String X_RAPIDAPI_KEY_VALUE = "f5b035e0dfmsh8c1500ff6d901c4p1ffb19jsna35d38f3fb91";
    // TODO: insert to retrieve historical data
    Context context;

    public StocksApi(Context context) {
        this.context = context;
    }

    public interface GetQuoteListener {
        void onError(String message);

        void onResponse(Stock stock);
    }

    public interface GetQuoteUserListener {
        void onError(String message);

        void onResponse(List<Stock.StockPreview> stocks);
    }

    public interface GetTrendingTickersListener {
        void onError(String message);

        void onResponse(List<Stock.StockPreview> stocks);
    }

    public interface GetAutoCompleteListener {
        void onError(String message);

        void onResponse(List<Stock> suggestedStocks);
    }

    public void getQuote(String ticker, GetQuoteListener getQuoteListener) {
        String url = QUERY_QUOTE + ticker;

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
                            getQuoteListener.onResponse(stock);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getQuoteListener.onError("Error!");
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
                        List<Stock.StockPreview> trendingStocks = new ArrayList<>();
                        try {
                            jsonArrayStocks = response.getJSONObject("finance").getJSONArray("result").getJSONObject(0).getJSONArray("quotes");
                            for (int i = 0; i < jsonArrayStocks.length(); i++) {
                                JSONObject stock = jsonArrayStocks.getJSONObject(i);
                                trendingStocks.add(new Stock.StockPreview(stock));
                            }
                            getTrendingTickersListener.onResponse(trendingStocks);
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

    public void getAutoComplete(String userInput, GetAutoCompleteListener getAutoCompleteListener) {
        String query = String.format(QUERY_AUTOCOMPLETE, userInput);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, query, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArraySuggestedStocks;
                        List<Stock> suggestedStocks = new ArrayList<>();
                        try {
                            jsonArraySuggestedStocks = response.getJSONArray("quotes");
                            for (int i = 0; i < jsonArraySuggestedStocks.length(); i++) {
                                //JSONObject stock = jsonArraySuggestedStocks.getJSONObject(i);
                                //suggestedStocks.add(new Stock(stock));
                            }
                            getAutoCompleteListener.onResponse(suggestedStocks);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getAutoCompleteListener.onError("Error!");
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

    public void getUserQuote(String ticker, GetQuoteUserListener getQuoteUserListener) {
        String url = QUERY_QUOTE + ticker;

        // Get JSON Object (curly braces {}) from API, JSON Array (square brackets [])
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArrayStocks;
                        List<Stock.StockPreview> favStocks = new ArrayList<>();

                        try {
                            jsonArrayStocks = response.getJSONObject("quoteResponse").getJSONArray("result");
                            for (int i = 0; i < jsonArrayStocks.length(); i++) {
                                JSONObject stock = jsonArrayStocks.getJSONObject(i);
                                favStocks.add(new Stock.StockPreview(stock));
                            }
                            getQuoteUserListener.onResponse(favStocks);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getQuoteUserListener.onError("Error!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-RapidAPI-Host", X_RAPIDAPI_HOST_VALUE);
                params.put("X-RapidAPI-Key", "e8fc6df944mshd56970fe068ee82p10748djsneb5118143f0e");

                return params;
            }
        };

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
