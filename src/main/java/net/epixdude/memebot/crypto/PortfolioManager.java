package net.epixdude.memebot.crypto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.dv8tion.jda.core.entities.Member;

public class PortfolioManager {
    
    private static Map<Member,Portfolio> portfolios = new ConcurrentHashMap<>();
    
    public void addCoin (Member owner, String symbol, Double amount) {
        portfolios.computeIfAbsent( owner, k -> new Portfolio(owner) );
        portfolios.get( owner ).addCoin( symbol, amount );
    }
    public void removeCoin (Member owner, String symbol) {
        portfolios.computeIfAbsent( owner, k -> new Portfolio(owner) );
        portfolios.get( owner ).removeCoin( symbol );
    }
    public String checkPortfolio(Member owner) {
        portfolios.computeIfAbsent( owner, k -> new Portfolio(owner) );
        return portfolios.get( owner ).checkPortfolio();
    }

}
