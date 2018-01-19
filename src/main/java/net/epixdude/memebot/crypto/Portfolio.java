package net.epixdude.memebot.crypto;

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
    protected void addCoin(String symbol, Double amount) {
        currencies.put( symbol, amount );
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
                final Double value = currencies.get( c ) * priceData.get( c );
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