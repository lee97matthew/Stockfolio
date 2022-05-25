package com.example.stockfolio;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String fullName, email;
    public List<Stock> favoritedStocks;

    // initialise user with no favoritedStocks
    public User() {
        this("", "");
    }

    public User(String fullName, String email) {
        List<Stock> favoritedStocks = new ArrayList<Stock>();
        favoritedStocks.add(new Stock());
        this.fullName = fullName;
        this.email = email;
        this.favoritedStocks = favoritedStocks;
    }

    public User(String fullName, String email, List<Stock> favoritedStocks) {
        this.fullName = fullName;
        this.email = email;
        this.favoritedStocks = favoritedStocks;
    }

    // insert function to add favoritedStock
    public User addFavoritedStock(Stock newFavouritedStock) {
        List<Stock> updatedFavoritedStocks = this.favoritedStocks;
        updatedFavoritedStocks.add(newFavouritedStock);
        return new User(this.fullName, this.email, updatedFavoritedStocks);
    }

    // insert function to remove favoritedStock
    public User removeFavoritedStock(Stock removeStock) {
        List<Stock> updatedFavoritedStocks = this.favoritedStocks;
        updatedFavoritedStocks.remove(removeStock);

        // check to ensure that the list of favoritedStocks is never empty so that it can be pushed to Firebase
        if (updatedFavoritedStocks.isEmpty()) {
            updatedFavoritedStocks.add(new Stock());
        }
        return new User(this.fullName, this.email, updatedFavoritedStocks);
    }

    // function to get the list of favoritedStocks
    public List<Stock> getFavoritedStocks() { return this.favoritedStocks; }
}
