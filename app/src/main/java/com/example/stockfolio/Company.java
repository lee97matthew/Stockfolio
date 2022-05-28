package com.example.stockfolio;

import java.util.ArrayList;
public class Company {

    private String companySymbol;
    public ArrayList<DailyPrice> companyStockPrices;

    public Company(String companySymbol, ArrayList<DailyPrice> companyStockPrices) {
        this.companySymbol = companySymbol;
        this.companyStockPrices = companyStockPrices;
    }
}