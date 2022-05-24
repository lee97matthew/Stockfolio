package com.example.stockfolio;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String fullName, email;
    public List<String> favTickers;

    public User() {

    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.favTickers = new ArrayList<>();
    }

    public User(String fullName, String email, String temp) {
        this.fullName = fullName;
        this.email = email;
        this.favTickers = new ArrayList<>();
        this.favTickers.add(temp);
    }

    public User(String fullName, String email, List<String> tickers) {
        this.fullName = fullName;
        this.email = email;
        this.favTickers = tickers;
    }

    public void favStock(String newStock) {
        this.favTickers.add(newStock);
    }

    public void unfavStock(String oldStock) {
        this.favTickers.remove(oldStock);
    }

    public List<String> getStocks() {
        return this.favTickers;
    }
}
