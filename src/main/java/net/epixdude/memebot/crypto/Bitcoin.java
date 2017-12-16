package net.epixdude.memebot.crypto;

public class Bitcoin extends Cryptocurrency {
	public Bitcoin() {
		super.TICKER_URL_STRING = "https://api.gdax.com/products/BTC-USD/ticker";
		super.STATS_URL_STRING = "https://api.gdax.com/products/BTC-USD/stats";
		super.FULL_NAME = "Bitcoin";
		super.ABBREVIATION = "BTC";
	}

}
