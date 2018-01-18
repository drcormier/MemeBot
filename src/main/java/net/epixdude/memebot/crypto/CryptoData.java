package net.epixdude.memebot.crypto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CryptoData {

    private double price, open, low, high, volume;
    private String currencyfullname, currencyabbreviation;

    public CryptoData() {
        this( 0.0, 0.0, 0.0, 0.0, 0.0, "", "" );
    }

    public CryptoData(double price, double open, double low, double high, double volume, String currencyfullname,
            String currencyabbreviation) {
        super();
        this.price = price;
        this.open = open;
        this.low = low;
        this.high = high;
        this.volume = volume;
        this.currencyfullname = currencyfullname;
        this.currencyabbreviation = currencyabbreviation;
    }

    public String getCurrencyabbreviation() {
        return currencyabbreviation;
    }

    public String getCurrencyfullname() {
        return currencyfullname;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getOpen() {
        return open;
    }

    public double getPrice() {
        return price;
    }

    public double getVolume() {
        return volume;
    }

    public void setCurrencyabbreviation(String currencyabbreviation) {
        this.currencyabbreviation = currencyabbreviation;
    }

    public void setCurrencyfullname(String currencyfullname) {
        this.currencyfullname = currencyfullname;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        final DecimalFormat format = new DecimalFormat("$###,##0.00####");
        final double daychange = (getPrice() / getOpen() - 1.0) * 100.0;
        String data = currencyfullname + " Stats:";
        data += "\nprice: `" + format.format( getPrice() );
        data += String.format( " (%+.2f%%)", daychange );
        data += "`\nopen: `" + format.format( getOpen() );
        data += "`\nhigh: `" + format.format( getHigh() );
        data += "`\nlow: `" + format.format( getLow() );
        data += String.format( "`\nvolume: `%,.2f " + currencyabbreviation + " (approx. $%,.2f)`", getVolume(),
                getVolume() * getPrice() );
        return data;
    }

}
