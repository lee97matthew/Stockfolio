package com.example.stockfolio.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stockfolio.RequestSingleton;
import com.example.stockfolio.Stocks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StocksApi {

    public static final String QUERY = "https://yh-finance.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=";

    Context context;
    double currentMarketPrice;

    public StocksApi(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(double currentMarketPrice);
    }

    public void getMarketPrice(String ticker, VolleyResponseListener volleyResponseListener) {
        String url = QUERY + ticker;

        // Get JSON Object (curly braces {}) from API, JSON Array (square brackets [])
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = response.getJSONObject("quoteResponse").getJSONArray("result").getJSONObject(0);
                            currentMarketPrice = jsonObject.getDouble("regularMarketPrice");
                            volleyResponseListener.onResponse(currentMarketPrice);
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
                params.put("X-RapidAPI-Host", "yh-finance.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "c0302ed34bmsh4ae1b7b7eb0514bp1ff5adjsn4d70320ec3d7");

                return params;
            }
        };

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

        // return currentMarketPrice;
    }
}
