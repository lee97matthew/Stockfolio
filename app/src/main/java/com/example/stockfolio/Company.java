public class Company {

    private String companySymbol;
    private ArrayList<DailyPrice> companyStockPrices;

    public Company(String companySymbol, ArrayList<DailyPrice> companyStockPrices) {
        this.companySymbol = companySymbol;
        this.companyStockPrices = companyStockPrices;
    }
}