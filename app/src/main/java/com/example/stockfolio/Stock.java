package com.example.stockfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Stock extends AppCompatActivity implements Parcelable {

    public static class StockPreview {
        private String symbol;
        private String shortName;
        private String typeDisp;

        public StockPreview(JSONObject jsonObject) throws JSONException {
            symbol = jsonObject.getString("symbol");
            shortName = jsonObject.getString("shortName");
            typeDisp = jsonObject.getString("typeDisp");
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getTypeDisp() {
            return typeDisp;
        }

        public void setTypeDisp(String typeDisp) {
            this.typeDisp = typeDisp;
        }
    }

    // Data field values of Parcel objects are FIFO (must be retrieved in the order they were put in)
    private String symbol;
    private double regularMarketPrice;
    private double regularMarketChange;
    // TODO - userFavorited - a boolean indicating if a stock has been favorited by the user, involves use of Firebase
    private boolean userFavorited;

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Stock(Parcel in) {
        symbol = in.readString();
        regularMarketPrice = in.readDouble();
        regularMarketChange = in.readDouble();
    }

    public Stock(JSONObject jsonObject) throws JSONException {
        symbol = jsonObject.getString("symbol");
        regularMarketPrice = jsonObject.getDouble("regularMarketPrice");
        regularMarketChange = jsonObject.getDouble("regularMarketChange");
    }

    public double getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getRegularMarketChange() {
        return regularMarketChange;
    }

    public boolean isUserFavorited() {
        return userFavorited;
    }

    /* everything below here is for implementing Parcelable */
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(symbol);
        out.writeDouble(regularMarketPrice);
        out.writeDouble(regularMarketChange);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

}