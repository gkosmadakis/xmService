package com.xm.recommendationservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xm.recommendationservice.service.CryptoServiceImpl;

/**
 * The CryptoController class is a REST controller responsible for handling API requests
 * related to cryptocurrency data.
 * It interacts with the CryptoService to fetch cryptocurrency statistics and other related data.
 */
@RestController
@RequestMapping("/crypto")
public class CryptoController {
	// The CryptoService that provides the core business logic for cryptocurrency operations.
    private final CryptoServiceImpl cryptoService;

    /**
     * Constructs a CryptoController instance with the provided CryptoService.
     *
     * @param cryptoService The service used to fetch cryptocurrency data.
     */
    public CryptoController(CryptoServiceImpl cryptoService) {
        this.cryptoService = cryptoService;
    }

    /**
     * Fetches statistics for a given cryptocurrency symbol.
     * 
     * This method makes a call to the CryptoService to retrieve stats such as current price,
     * market cap, volume, etc., for the requested cryptocurrency symbol.
     *
     * @param symbol The symbol of the cryptocurrency (e.g., "BTC", "ETH").
     * @return A map containing various statistics for the given cryptocurrency symbol.
     */
    @GetMapping("/stats/{symbol}")
    public Map<String, Object> getCryptoStats(@PathVariable String symbol) {
        return cryptoService.getCryptoStats(symbol);
    }
    /**
     * Fetches a list of the top cryptocurrencies based on some criteria, such as market capitalization
     * or trading volume.
     * 
     * @return A list of top cryptocurrency symbols (e.g., ["BTC", "ETH", "XRP"]).
     */
    @GetMapping("/top")
    public List<String> getTopCryptos() {
        return cryptoService.getTopCryptos();
    }
    /**
     * Fetches the cryptocurrency with the highest price range on a specific date.
     * 
     * The date is passed as a parameter, and the method returns the cryptocurrency with the highest
     * price fluctuation on that date.
     *
     * @param date The date (in timestamp format) for which to retrieve the highest price range.
     * @return The symbol of the cryptocurrency with the highest price range on that date.
     */
    @GetMapping("/highest-range/{date}")
    public String getCryptoWithHighestRange(@PathVariable long date) {
    	// Calling the CryptoService to get the crypto with the highest price range for the given date
    	return cryptoService.getCryptoWithHighestRange(date);
    }
}
