package com.example.stockfolio.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stockfolio.RequestSingleton;
import com.example.stockfolio.Stock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StocksApi {

    public static final String QUERY = "https://yh-finance.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=";
    private static final String X_RAPIDAPI_HOST_VALUE = "yh-finance.p.rapidapi.com";
    private static final String X_RAPIDAPI_KEY_VALUE = "c0302ed34bmsh4ae1b7b7eb0514bp1ff5adjsn4d70320ec3d7";

    Context context;
    double currentMarketPrice;
    Stock stock;

    public StocksApi(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(Stock stock);
    }

    public void retrieveTickerSymbol(String ticker, VolleyResponseListener volleyResponseListener) {
        String url = QUERY + ticker;

        // Get JSON Object (curly braces {}) from API, JSON Array (square brackets [])
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = response.getJSONObject("quoteResponse").getJSONArray("result").getJSONObject(0);
                            stock = new Stock(jsonObject);
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
}
