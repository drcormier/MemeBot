package net.epixdude.memebot.crypto;

public class Ethereum extends Cryptocurrency{
	public Ethereum() {
		super.TICKER_URL_STRING = "https://api.gdax.com/products/ETH-USD/ticker";
		super.STATS_URL_STRING = "https://api.gdax.com/products/ETH-USD/stats";
		super.FULL_NAME = "Ethereum";
		super.ABBREVIATION = "ETH";
	}
	
}
