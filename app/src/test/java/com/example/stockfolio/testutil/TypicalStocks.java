package com.example.stockfolio.testutil;

import com.example.stockfolio.Stock;

import org.json.JSONException;
import org.json.JSONObject;

public class TypicalStocks {

    // keys for the JSONObject
    public static final String KEY_SYMBOL = "symbol";
    public static final String KEY_MARKETPRICE= "regularMarketPrice";
    public static final String KEY_MARKETCHANGE= "regularMarketChange";

    // valid values for various stocks
    public static final String VALID_SYMBOL_TSLA = "TSLA";
    public static final double VALID_MARKETPRICE_TSLA = 690.42;
    public static final double VALID_MARKETCHANGE_TSLA = 32.5;
    public static final String VALID_SYMBOL_MSFT = "MSFT";
    public static final double VALID_MARKETPRICE_MSFT = 262.52;
    public static final double VALID_MARKETCHANGE_MSFT = 1.09;
    public static final String VALID_SYMBOL_AAPL = "AAPL";
    public static final double VALID_MARKETPRICE_AAPL = 140.33;
    public static final double VALID_MARKETCHANGE_AAPL = -1.43;

    // invalid values for various stocks
    public static final String INVALID_SYMBOL_TSLA = "Tesla";
    public static final double INVALID_MARKETPRICE_TSLA = 690.42;
    public static final double INVALID_MARKETCHANGE_TSLA = 32.5;
    public static final String INVALID_SYMBOL_MSFT = "Microsoft";
    public static final double INVALID_MARKETPRICE_MSFT = 262.52;
    public static final double INVALID_MARKETCHANGE_MSFT = 1.09;
    public static final String INVALID_SYMBOL_AAPL = "Apple";
    public static final double INVALID_MARKETPRICE_AAPL = 140.33;
    public static final double INVALID_MARKETCHANGE_AAPL = -1.43;

    public static Stock TSLA;
    public static Stock MSFT;
    public static Stock AAPL;

    static {
        try {
            TSLA = new Stock(new JSONObject()
                    .put(KEY_SYMBOL, VALID_SYMBOL_TSLA)
                    .put(KEY_MARKETPRICE, VALID_MARKETPRICE_TSLA)
                    .put(KEY_MARKETCHANGE, VALID_MARKETCHANGE_TSLA));
            MSFT = new Stock(new JSONObject()
                    .put(KEY_SYMBOL, VALID_SYMBOL_MSFT)
                    .put(KEY_MARKETPRICE, VALID_MARKETPRICE_MSFT)
                    .put(KEY_MARKETCHANGE, VALID_MARKETCHANGE_MSFT));
            AAPL = new Stock(new JSONObject()
                    .put(KEY_SYMBOL, VALID_SYMBOL_AAPL)
                    .put(KEY_MARKETPRICE, VALID_MARKETPRICE_AAPL)
                    .put(KEY_MARKETCHANGE, VALID_MARKETCHANGE_AAPL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
