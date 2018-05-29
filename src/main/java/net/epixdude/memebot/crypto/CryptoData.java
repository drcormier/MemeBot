package net.epixdude.memebot.crypto;

public class CryptoData
{
    private String name;
    private double price, change;

    public CryptoData(String name, double price, double change)
    {
        this.name = name;
        this.price = price;
        this.change = change;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public double getChange()
    {
        return change;
    }
}
