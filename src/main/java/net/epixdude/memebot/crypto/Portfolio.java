package net.epixdude.memebot.crypto;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.Member;


public class Portfolio {
    
    private Map<String, Double> currencies = new HashMap<>();
    private Member owner;
    
    protected Portfolio( Member owner ) {
        this.owner = owner;
    }
    
    protected void addCoin ( String symbol, Double amount) {
        currencies.put( symbol, amount );
    }
    
    protected void removeCoin (String symbol) {
        currencies.remove( symbol );
    }
    
    protected String checkPortfolio() {
        try {
            Map<String, Double> priceData = BulkCryptoCurrencyPriceGetter.getPriceData( currencies.keySet() );
            String output = BulkCryptoCurrencyPriceGetter.getPrices( priceData );
            final DecimalFormat format = new DecimalFormat( "$###,##0.00####" );
            Double sum = 0.0;
            for( String c : currencies.keySet() ) {
                Double value = currencies.get( c )*priceData.get( c );
                sum += value;
                output += "\nYour " + currencies.get( c ).toString() + " " + c + " is worth " + format.format( value );
            }
            output += "\nYour portfolio is worth ";
            output += format.format( sum );
            return output;
        } catch ( Exception e ) {
            e.printStackTrace();
            return "An error has occured";
        }
    }
}