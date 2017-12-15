package net.epixdude.memebot.ethereum;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class Ethereum {

	private static final String ETHEREUM_TICKER = "https://api.gdax.com/products/ETH-USD/ticker";
	private static final String ETHEREUM_STATS = "https://api.gdax.com/products/ETH-USD/stats";
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static EthereumData getEthereumData() throws Exception{
		EthereumData data = new EthereumData();
		URL tickerURL = new URL(ETHEREUM_TICKER);
		HttpURLConnection tickerCon = (HttpURLConnection) tickerURL.openConnection();
		tickerCon.setRequestMethod("GET");
		tickerCon.setRequestProperty("User-Agent", USER_AGENT);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(tickerCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONObject j = new JSONObject(response.toString());
		data.setPrice(j.getDouble("price"));

		URL statsURL = new URL(ETHEREUM_STATS);
		HttpURLConnection statsCon = (HttpURLConnection) statsURL.openConnection();
		statsCon.setRequestMethod("GET");
		statsCon.setRequestProperty("User-Agent", USER_AGENT);
		in = new BufferedReader(
		        new InputStreamReader(statsCon.getInputStream()));
		response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		j = new JSONObject(response.toString());
		data.setOpen(j.getDouble("open"));
		data.setHigh(j.getDouble("high"));
		data.setLow(j.getDouble("low"));
		data.setVolume(j.getDouble("volume"));
		return data;
	}
}
