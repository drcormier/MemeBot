package net.epixdude.memebot.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

public class Dogecoin extends Cryptocurrency {
    
    public Dogecoin() {
        super.STATS_URL_STRING = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=DOGE&tsyms=USD";
        super.FULL_NAME = "Dogecoin";
        super.ABBREVIATION = "DOGE";
    }

    @Override
    public CryptoData getData() throws ProtocolException, IOException, MalformedURLException {
        final CryptoData data = new CryptoData();
        data.setCurrencyfullname( FULL_NAME );
        data.setCurrencyabbreviation( ABBREVIATION );

        // grab current stats
        final URL statsURL = new URL( STATS_URL_STRING );
        final HttpURLConnection statsCon = (HttpURLConnection) statsURL.openConnection();
        statsCon.setRequestMethod( "GET" );
        statsCon.setRequestProperty( "User-Agent", USER_AGENT );
        BufferedReader in = new BufferedReader( new InputStreamReader( statsCon.getInputStream() ) );
        StringBuffer response = new StringBuffer();

        String inputLine;
        while ( (inputLine = in.readLine()) != null ) {
            response.append( inputLine );
        }
        in.close();
        JSONObject j = new JSONObject( response.toString() );
        data.setPrice( j.getJSONObject( "RAW" ).getJSONObject( "DOGE" ).getJSONObject( "USD" ).getDouble( "PRICE" ) );
        data.setOpen( j.getJSONObject( "RAW" ).getJSONObject( "DOGE" ).getJSONObject( "USD" ).getDouble( "OPEN24HOUR" ) );
        data.setHigh( j.getJSONObject( "RAW" ).getJSONObject( "DOGE" ).getJSONObject( "USD" ).getDouble( "HIGH24HOUR" ) );
        data.setLow( j.getJSONObject( "RAW" ).getJSONObject( "DOGE" ).getJSONObject( "USD" ).getDouble( "LOW24HOUR" ) );
        data.setVolume( j.getJSONObject( "RAW" ).getJSONObject( "DOGE" ).getJSONObject( "USD" ).getDouble( "VOLUME24HOUR" ) );
        return data;
    }

}
