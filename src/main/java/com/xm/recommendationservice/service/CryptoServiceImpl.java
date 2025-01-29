package com.xm.recommendationservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xm.recommendationservice.repository.CryptoRepository;
/**
 * The CryptoServiceImpl class is an implementation of the CryptoService interface.
 * It acts as a service layer for interacting with the cryptocurrency data repository (CryptoRepository).
 * This class retrieves data from the repository and provides business logic methods to obtain
 * cryptocurrency statistics, the top cryptocurrencies, and the one with the highest price range.
 */
@Service
public class CryptoServiceImpl implements CryptoService {
	// The CryptoRepository that provides access to cryptocurrency price data.
    private final CryptoRepository cryptoRepository;
    /**
     * Constructs a CryptoServiceImpl instance with the provided CryptoRepository.
     * 
     * @param cryptoRepository The repository used to fetch cryptocurrency data.
     */
    public CryptoServiceImpl(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }
    /**
     * Retrieves the statistics (min, max price, and timestamps) for a given cryptocurrency symbol.
     * This method calls the repository to fetch the statistics and then returns them.
     *
     * @param symbol The symbol of the cryptocurrency (e.g., "BTC", "ETH").
     * @return A map containing statistics for the given cryptocurrency symbol.
     *         The map includes the symbol, min price, max price, oldest timestamp, and newest timestamp.
     */
    public Map<String, Object> getCryptoStats(String symbol) {
    	 // Delegates the call to the repository to get the cryptocurrency stats
        return cryptoRepository.getCryptoStats(symbol);
    }
    /**
     * Retrieves the list of top cryptocurrencies, sorted by their normalized price range.
     * This method calls the repository to fetch the top cryptocurrencies based on the price fluctuations.
     * 
     * @return A list of cryptocurrency symbols sorted by their normalized price range (highest to lowest).
     */
    public List<String> getTopCryptos() {
    	 // Delegates the call to the repository to get the top cryptocurrencies
        return cryptoRepository.getTopCryptos();
    }
    /**
     * Retrieves the cryptocurrency with the highest price range for a specific date.
     * This method calls the repository to fetch the cryptocurrency with the highest price fluctuation
     * on the given date.
     * 
     * @param date The timestamp of the date for which to find the cryptocurrency with the highest range.
     * @return The symbol of the cryptocurrency with the highest price range on that date,
     *         or a message indicating that no data is available.
     */
    public String getCryptoWithHighestRange(long date) {
    	// Delegates the call to the repository to get the crypto with the highest range for the given date
        return cryptoRepository.getCryptoWithHighestRange(date);
    }
}
