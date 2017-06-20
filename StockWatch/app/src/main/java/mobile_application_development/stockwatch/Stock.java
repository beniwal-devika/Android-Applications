package mobile_application_development.stockwatch;

import java.io.Serializable;

/**
 * Created by devikabeniwal on 17/03/17.
 */

public class Stock implements Serializable {
    private  String symbol;
    private String companyName;
    private Double price;
    private Double change;
    private Double percentage;


    public Stock(String sym, String cname)
    {
        this.symbol = sym;
        this.companyName = cname;
    }

    public Stock(String sym, String cname, Double pr, Double ch, Double per) {
        this.symbol = sym;
        this.companyName = cname;
        this.price = pr;
        this.change = ch;
        this.percentage = per;
    }

    /*public Stock() {

    }*/

    @Override
    public String toString() {
        return "Stock{" +
                "Symbol='" + symbol + '\'' +
                ", CName='" + companyName + '\'' +
                ", Price=" + price +
                ", Change=" + change +
                ", Percentage=" + percentage +
                '}';
    }

    public  String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Double getPrice() {
        return price;
    }

    public Double getChange() {
        return change;
    }

    public Double getPercentage() {
        return percentage;
    }


    public void setSymbol(String sym) {
        this.symbol = symbol;
    }

    public void setCompanyName(String CName) {
        this.companyName = CName;
    }

    public void setPrice(Double cost) {
        this.price = cost;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}

