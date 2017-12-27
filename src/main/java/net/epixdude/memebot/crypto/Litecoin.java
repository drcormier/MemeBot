package net.epixdude.memebot.crypto;

public class Litecoin extends Cryptocurrency {

    public Litecoin() {
        super.TICKER_URL_STRING = "https://api.gdax.com/products/LTC-USD/ticker";
        super.STATS_URL_STRING = "https://api.gdax.com/products/LTC-USD/stats";
        super.FULL_NAME = "Litecoin";
        super.ABBREVIATION = "LTC";
    }

}
