package net.epixdude.memebot.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class BulkCryptoCurrencyPriceGetter {

    // url patterns for the various parts of the cryptocompare api url
    // a valid url looks like this:
    // https://min-api.cryptocompare.com/data/pricemulti?fsyms=ETH,DASH&tsyms=BTC,USD,EUR
    /**
     * The first half of the cyptocompare api url. A valid url should look like
     * this:
     * https://min-api.cryptocompare.com/data/pricemulti?fsyms=ETH,DASH&tsyms=BTC,USD,EUR
     */
    private static final String URL_FIRST_PART = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=";
    // symbols for the cryptocurrency go between the above and below strings
    /**
     * The second half of the cyptocompare api url. A valid url should look like
     * this:
     * https://min-api.cryptocompare.com/data/pricemulti?fsyms=ETH,DASH&tsyms=BTC,USD,EUR
     */
    private static final String URL_SECOND_PART = "&tsyms=USD";
    private static final String USD = "USD";
    private static final String USER_AGENT = "Mozilla/5.0";

    /**
     * Gets the prices of various crpytocurrencies from CryptoCompare
     *
     * @param currencies
     *            the case-sensitive symbols for cryptocurrencies the user wishes to
     *            look up
     * @return the message to print to the text channel
     * @throws ProtocolException
     * @throws IOException
     * @throws MalformedURLException
     */
    public static String getPrices(Iterable<String> currencies)
            throws ProtocolException, IOException, MalformedURLException {
        // build the url to use
        String urlToFetch = URL_FIRST_PART;
        // symbols are added as comma separated values
        for ( final String c : currencies ) {
            urlToFetch += c;
            urlToFetch += ",";
        }
        // remove the last comma
        urlToFetch = urlToFetch.substring( 0, urlToFetch.length() - 1 );
        urlToFetch += URL_SECOND_PART;
        // connect to the website
        final URL statsURL = new URL( urlToFetch );
        final HttpURLConnection statsCon = (HttpURLConnection) statsURL.openConnection();
        statsCon.setRequestMethod( "GET" );
        statsCon.setRequestProperty( "User-Agent", USER_AGENT );
        // get the data
        final BufferedReader in = new BufferedReader( new InputStreamReader( statsCon.getInputStream() ) );
        final StringBuffer response = new StringBuffer();

        // read the data into a buffer
        String inputLine;
        while ( (inputLine = in.readLine()) != null ) {
            response.append( inputLine );
        }
        in.close();
        // parse the json
        final JSONObject j = new JSONObject( response.toString() );
        // see if the api returned an error. if so, send the error to the user
        try {
            return j.getString( "Message" );
        } catch ( final JSONException jse ) {
        }
        // take the json and build the output
        String output = "```\n";
        final DecimalFormat format = new DecimalFormat( "$###,##0.00####" );
        for ( final String c : currencies ) {
            output += String.format( "%-8s", c + ":" ) + format.format( j.getJSONObject( c ).getDouble( USD ) ) + "\n";
        }
        output += "```";
        return output;
    }

}
