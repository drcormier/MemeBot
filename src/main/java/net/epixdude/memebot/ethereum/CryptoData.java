package net.epixdude.memebot.ethereum;

public class CryptoData {

	private double price,open,low,high,volume;
	
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

	public CryptoData(double price, double open, double low, double high, double volume) {
		super();
		this.price = price;
		this.open = open;
		this.low = low;
		this.high = high;
		this.volume = volume;
	}
	
	public CryptoData(){
		this(0.0,0.0,0.0,0.0,0.0);
	}

}
