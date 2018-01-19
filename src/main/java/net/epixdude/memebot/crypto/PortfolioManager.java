package net.epixdude.memebot.crypto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;

public class PortfolioManager {

    private static Map<Member, Portfolio> portfolios = new ConcurrentHashMap<>();

    /**
     * Add a coin to a user's portfolio
     * 
     * @param owner
     *            the owner of the portfolio
     * @param symbol
     *            the symbol of the coin to add
     * @param amount
     *            the amount of coin to add
     */
    public void addCoin(Member owner, String symbol, Double amount) {
        portfolios.computeIfAbsent( owner, k -> new Portfolio() );
        portfolios.get( owner ).addCoin( symbol, amount );
    }

    /**
     * Checks a user's portfolio
     * 
     * @param owner
     *            the owner of the portfolio
     * @return the value of the portfolio, designed to be sent to a
     *         {@link MessageChannel}
     */
    public String checkPortfolio(Member owner) {
        portfolios.computeIfAbsent( owner, k -> new Portfolio() );
        return portfolios.get( owner ).checkPortfolio();
    }

    /**
     * Remove a coin from a user's portfolio
     * 
     * @param owner
     *            the owner of the portfolio
     * @param symbol
     *            the symbol of the coin to remove
     */
    public void removeCoin(Member owner, String symbol) {
        portfolios.computeIfAbsent( owner, k -> new Portfolio() );
        portfolios.get( owner ).removeCoin( symbol );
    }

}
