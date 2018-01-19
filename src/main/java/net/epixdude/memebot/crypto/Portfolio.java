package net.epixdude.memebot.crypto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for the {@link HashMap} class that handles a user's portfolio of
 * cryptocurrencies. Should only be accessed through a {@link PortfolioManager}
 * instance.
 *
 */
public class Portfolio {

    private final Map<String, Double> currencies = new HashMap<>();

    /**
     * Adds a coin to the portfolio.
     * 
     * @param symbol
     *            the symbol of the coin to add
     * @param amount
     *            the amount of coin to add
     */
    protected String addCoin(String symbol, Double amount) {
        try {
            if(BulkCryptoCurrencyPriceGetter.isValidSymbol( symbol )) {
                currencies.put( symbol, amount );
                return "Successfully added " + amount.toString() + " " + symbol;
            }else {
                return symbol + " is not a valid symbol!";
            }
        } catch ( ProtocolException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return "An error has occured while adding that coin";
    }

    /**
     * Checks the value of the portfolio.
     * 
     * @return a string representation of the portfolio, suitable for sending
     *         through a MessageChannel
     */
    protected String checkPortfolio() {
        try {
            final Map<String, Double> priceData = BulkCryptoCurrencyPriceGetter.getPriceData( currencies.keySet() );
            String output = BulkCryptoCurrencyPriceGetter.getPrices( priceData );
            final DecimalFormat format = new DecimalFormat( "$###,##0.00####" );
            Double sum = 0.0;
            for ( final String c : currencies.keySet() ) {
                Double quantity = currencies.get( c );
                Double price = priceData.get( c );
                Double value = quantity*price;
                sum += value;
                output += "\nYour " + currencies.get( c ).toString() + " " + c + " is worth " + format.format( value );
            }
            output += "\nYour portfolio is worth ";
            output += format.format( sum );
            return output;
        } catch ( final Exception e ) {
            e.printStackTrace();
            return "An error has occured";
        }
    }

    /**
     * Removes a coin from the portfolio.
     * 
     * @param symbol
     *            the symbol of the coin to remove
     */
    protected void removeCoin(String symbol) {
        currencies.remove( symbol );
    }
}