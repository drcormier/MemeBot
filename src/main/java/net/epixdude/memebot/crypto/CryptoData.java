package net.epixdude.memebot.crypto;

import java.text.NumberFormat;
import java.util.Locale;

public class CryptoData {

	private double price,open,low,high,volume;
	private String currencyfullname, currencyabbreviation;
	
	public String getCurrencyfullname() {
		return currencyfullname;
	}

	public void setCurrencyfullname(String currencyfullname) {
		this.currencyfullname = currencyfullname;
	}

	public String getCurrencyabbreviation() {
		return currencyabbreviation;
	}

	public void setCurrencyabbreviation(String currencyabbreviation) {
		this.currencyabbreviation = currencyabbreviation;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public CryptoData(double price, double open, double low, double high, double volume, String currencyfullname, String currencyabbreviation) {
		super();
		this.price = price;
		this.open = open;
		this.low = low;
		this.high = high;
		this.volume = volume;
		this.currencyfullname = currencyfullname;
		this.currencyabbreviation = currencyabbreviation;
	}
	
	public CryptoData(){
		this(0.0,0.0,0.0,0.0,0.0, "", "");
	}
	
	public String toString() {
            		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
		double daychange = ((this.getPrice()/this.getOpen())-1.0)*100.0;
		String data = this.currencyfullname + " Stats:";
		data += "\nprice: `" + format.format(this.getPrice());
		data += String.format(" (%+.2f%%)", daychange );
		data += "`\nopen: `" + format.format(this.getOpen());
		data += "`\nhigh: `" + format.format(this.getHigh());
		data += "`\nlow: `" + format.format(this.getLow());
		data += String.format("`\nvolume: `%,.2f " + this.currencyabbreviation + 
				"(approx. $%,.2f)`", this.getVolume(), this.getVolume()*this.getPrice() );
		return data;
	}

}
