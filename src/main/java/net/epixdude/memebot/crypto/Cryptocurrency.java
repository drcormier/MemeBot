package net.epixdude.memebot.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

public abstract class Cryptocurrency {

    private static final String USER_AGENT = "Mozilla/5.0";
    protected String TICKER_URL_STRING;
    protected String STATS_URL_STRING;
    protected String FULL_NAME;
    protected String ABBREVIATION;

    public CryptoData getData() throws ProtocolException, IOException, MalformedURLException {
        // get JSON from eth ticker
        final CryptoData data = new CryptoData();
        data.setCurrencyfullname( FULL_NAME );
        data.setCurrencyabbreviation( ABBREVIATION );
        final URL tickerURL = new URL( TICKER_URL_STRING );
        final HttpURLConnection tickerCon = (HttpURLConnection) tickerURL.openConnection();
        tickerCon.setRequestMethod( "GET" );
        tickerCon.setRequestProperty( "User-Agent", USER_AGENT );
        // save ticker to string
        BufferedReader in = new BufferedReader( new InputStreamReader( tickerCon.getInputStream() ) );
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ( (inputLine = in.readLine()) != null ) {
            response.append( inputLine );
        }
        in.close();
        // parse JSON
        JSONObject j = new JSONObject( response.toString() );
        // grab current price
        data.setPrice( j.getDouble( "price" ) );

        // grab current stats
        final URL statsURL = new URL( STATS_URL_STRING );
        final HttpURLConnection statsCon = (HttpURLConnection) statsURL.openConnection();
        statsCon.setRequestMethod( "GET" );
        statsCon.setRequestProperty( "User-Agent", USER_AGENT );
        in = new BufferedReader( new InputStreamReader( statsCon.getInputStream() ) );
        response = new StringBuffer();

        while ( (inputLine = in.readLine()) != null ) {
            response.append( inputLine );
        }
        in.close();
        j = new JSONObject( response.toString() );
        data.setOpen( j.getDouble( "open" ) );
        data.setHigh( j.getDouble( "high" ) );
        data.setLow( j.getDouble( "low" ) );
        data.setVolume( j.getDouble( "volume" ) );
        return data;
    }
}
