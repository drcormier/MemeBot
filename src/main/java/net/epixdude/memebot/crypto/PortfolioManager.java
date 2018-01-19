package net.epixdude.memebot.crypto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class PortfolioManager {

    private static Map<Long, Portfolio> portfolios = new ConcurrentHashMap<>();
    private static final Type mapType = new TypeToken<Map<Long, Portfolio>>() {
    }.getType();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String addCoin(Long idLong, String symbol, Double amount) {
        portfolios.computeIfAbsent( idLong, k -> new Portfolio() );
        return portfolios.get( idLong ).addCoin( symbol, amount );
    }

    public String checkPortfolio(Long idLong) {
        portfolios.computeIfAbsent( idLong, k -> new Portfolio() );
        return portfolios.get( idLong ).checkPortfolio();
    }

    public synchronized void loadPortfolios() {
        try ( JsonReader reader = new JsonReader( new FileReader( "pm.json" ) ) ) {
            portfolios = gson.fromJson( reader, mapType );
            System.out.println( "Read in portfolios from pm.json" );
        } catch ( final FileNotFoundException e ) {
            System.err.println( "pm.json does not exist." );
        } catch ( final IOException e ) {
            e.printStackTrace();
        }

    }

    public void removeCoin(Long idLong, String symbol) {
        portfolios.computeIfAbsent( idLong, k -> new Portfolio() );
        portfolios.get( idLong ).removeCoin( symbol );
    }

    public void resetPortfolio(Long idLong) {
        portfolios.put( idLong, new Portfolio() );
    }

    public synchronized void savePortfolios() {
        try ( JsonWriter writer = new JsonWriter( new FileWriter( "pm.json" ) ) ) {
            gson.toJson( portfolios, mapType, writer );
        } catch ( final IOException e ) {
            e.printStackTrace();
        }
    }

}
