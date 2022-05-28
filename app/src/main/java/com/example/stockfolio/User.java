package com.example.stockfolio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String fullName, email;
    public List<String> favoritedStocks;

    // initialise user with no favoritedStocks
    public User() {
        this("", "");
    }

    public User(String fullName, String email) {
        List<String> favoritedStocks = new ArrayList<String>();
        favoritedStocks.add("test");
        this.fullName = fullName;
        this.email = email;
        this.favoritedStocks = favoritedStocks;

    }

    public User(String fullName, String email, List<String> favoritedStocks) {
        this.fullName = fullName;
        this.email = email;
        this.favoritedStocks = favoritedStocks;
    }

    // insert function to add favoritedStock
    public User addFavoritedStock(String newFavoritedStock) {
        List<String> updatedFavoritedStocks = this.favoritedStocks;
        updatedFavoritedStocks.add(newFavoritedStock);
        return new User(this.fullName, this.email, updatedFavoritedStocks);
    }

    // insert function to remove favoritedStock
    public User removeFavoritedStock(String removeStock) {
        List<String> updatedFavoritedStocks = this.favoritedStocks;
        updatedFavoritedStocks.remove(removeStock);

        // check to ensure that the list of favoritedStocks is never empty so that it can be pushed to Firebase
        if (updatedFavoritedStocks.isEmpty()) {
            updatedFavoritedStocks.add("test");
        }
        return new User(this.fullName, this.email, updatedFavoritedStocks);
    }

    // function for getters and setters
    public List<String> getFavoritedStocks() {
        return this.favoritedStocks;
    }
    public void setFavoritedStocks(List<String> favoritedStocks) {
        this.favoritedStocks = favoritedStocks;
    }

    public String getFullName() { return this.fullName; }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() { return this.email; }

    public void setEmail(String email) {
        this.email = email;
    }
}
