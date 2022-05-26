package com.example.stockfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static com.example.stockfolio.testutil.TypicalStocks.TSLA;
import static com.example.stockfolio.testutil.TypicalStocks.MSFT;
import static com.example.stockfolio.testutil.TypicalStocks.AAPL;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_MARKETCHANGE_AAPL;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_MARKETCHANGE_MSFT;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_MARKETCHANGE_TSLA;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_MARKETPRICE_TSLA;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_MARKETPRICE_MSFT;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_MARKETPRICE_AAPL;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_SYMBOL_AAPL;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_SYMBOL_MSFT;
import static com.example.stockfolio.testutil.TypicalStocks.VALID_SYMBOL_TSLA;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class StockTest {

    private static final double DELTA = 1e-15;

    @Test
    public void initialize_validTickers_success() {
        try {
            // Tesla (TSLA) with complete values
            JSONObject jsonObjectTSLA = new JSONObject()
                    .put("symbol", "TSLA")
                    .put("regularMarketPrice", 690.42)
                    .put("regularMarketChange", 32.5);
            Stock stockTSLA = new Stock(jsonObjectTSLA);

            assertTrue(stockTSLA instanceof Stock);
        } catch (JSONException e) {
            fail("Unexpected JSONException was thrown!");
        }
        try {
            // Apple (AAPL) with complete values
            JSONObject jsonObjectAAPL = new JSONObject()
                    .put("symbol", "AAPL")
                    .put("regularMarketPrice", 140.52)
                    .put("regularMarketChange", -1.10);
            Stock stockAAPL = new Stock(jsonObjectAAPL);

            assertTrue(stockAAPL instanceof Stock);
        } catch (JSONException e) {
            assertNotNull(e);
        }
        try {
            // Microsoft (MSFT) with complete values
            JSONObject jsonObjectMSFT = new JSONObject()
                    .put("symbol", "MSFT")
                    .put("regularMarketPrice", 140.52)
                    .put("regularMarketChange", -1.10);
            Stock stockMSFT = new Stock(jsonObjectMSFT);

            assertTrue(stockMSFT instanceof Stock);
        } catch (JSONException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void initialize_incorrectValueTypes_failure() {
        try {
            // Tesla (TSLA) with incorrect value type for regularMarketPrice
            JSONObject jsonObjectTSLA = new JSONObject()
                    .put("symbol", "TSLA")
                    .put("regularMarketPrice", true)
                    .put("regularMarketChange", 32.5);
            Stock stockTSLA = new Stock(jsonObjectTSLA);

            fail("Unexpected JSONException was not thrown");
        } catch (JSONException e) {
            assertNotNull(e);
        }
        try {
            // Apple (AAPL) with incorrect value type for regularMarketChange
            JSONObject jsonObjectAAPL = new JSONObject()
                    .put("symbol", "AAPL")
                    .put("regularMarketPrice", 140.33)
                    .put("regularMarketChange", true);
            Stock stockAAPL = new Stock(jsonObjectAAPL);

            fail("Unexpected JSONException was not thrown");
        } catch (JSONException e) {
            assertNotNull(e);
        }
        try {
            // Microsoft (MSFT) with incorrect value type for symbol
            JSONObject jsonObjectMSFT = new JSONObject()
                    .put("symbol", 100)
                    .put("regularMarketPrice", 262.52)
                    .put("regularMarketChange", 1.09);
            Stock stockMSFT = new Stock(jsonObjectMSFT);

            fail("Unexpected JSONException was not thrown");
        } catch (JSONException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void initialize_missingValues_failure() {
        try {
            // Tesla (TSLA), with missing "regularMarketChange" value
            JSONObject jsonObjectTSLA = new JSONObject()
                    .put("symbol", "TSLA")
                    .put("regularMarketPrice", 690.42);
            Stock stockTSLA = new Stock(jsonObjectTSLA);

            assertTrue(stockTSLA instanceof Stock);

            fail("Unexpected JSONException was not thrown");
        } catch (JSONException e) {
            assertNotNull(e);
        }
        try {
            // Apple (AAPL), with missing "regularMarketPrice" value
            JSONObject jsonObjectAAPL = new JSONObject()
                    .put("symbol", "AAPL")
                    .put("regularMarketChange", -1.10);
            Stock stockAAPL = new Stock(jsonObjectAAPL);

            assertTrue(stockAAPL instanceof Stock);

            fail("Unexpected JSONException was not thrown");
        } catch (JSONException e) {
            assertNotNull(e);
        }
        try {
            // Microsoft (MSFT), with missing "symbol" value
            JSONObject jsonObjectMSFT = new JSONObject()
                    .put("regularMarketChange", -1.10)
                    .put("regularMarketPrice", 690.42);
            Stock stockMSFT = new Stock(jsonObjectMSFT);

            assertTrue(stockMSFT instanceof Stock);

            fail("Unexpected JSONException was not thrown");
        } catch (JSONException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void getRegularMarketPrice_completeValues_success() {
        assertEquals(VALID_MARKETPRICE_TSLA, TSLA.getRegularMarketPrice(), DELTA);
        assertEquals(VALID_MARKETPRICE_MSFT, MSFT.getRegularMarketPrice(), DELTA);
        assertEquals(VALID_MARKETPRICE_AAPL, AAPL.getRegularMarketPrice(), DELTA);
    }

    @Test
    public void getSymbol_completeValues_success() {
        assertEquals(VALID_SYMBOL_TSLA, TSLA.getSymbol());
        assertEquals(VALID_SYMBOL_MSFT, MSFT.getSymbol());
        assertEquals(VALID_SYMBOL_AAPL, AAPL.getSymbol());
    }

    @Test
    public void getRegularMarketChange_completeValues_success() {
        assertEquals(VALID_MARKETCHANGE_TSLA, TSLA.getRegularMarketChange(), DELTA);
        assertEquals(VALID_MARKETCHANGE_MSFT, MSFT.getRegularMarketChange(), DELTA);
        assertEquals(VALID_MARKETCHANGE_AAPL, AAPL.getRegularMarketChange(), DELTA);
    }

    @Test
    public void toString_completeValues_success() {
        assertTrue(TSLA.toString() instanceof String);
        assertTrue(MSFT.toString() instanceof String);
        assertTrue(AAPL.toString() instanceof String);
    }

    @Test
    public void isUserFavorited() {
        // TODO - update or remove unit test upon confirmation of implementation of favorited stocks
    }
}