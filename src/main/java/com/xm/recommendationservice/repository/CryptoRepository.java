package com.xm.recommendationservice.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
/**
 * The CryptoRepository class is responsible for managing and retrieving cryptocurrency price data.
 * It loads the data from CSV files, computes basic statistics like price ranges, and provides methods
 * to fetch statistics for individual cryptocurrencies.
 * 
 * The repository holds cryptocurrency prices and corresponding timestamps in memory, and supports 
 * fetching data about individual cryptocurrencies and calculating their price range.
 */
@Repository
public class CryptoRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(CryptoRepository.class);  // Logger for this class
	// A map storing the prices of cryptocurrencies, where the key is the cryptocurrency symbol
    // (e.g., "BTC") and the value is a list of prices for that cryptocurrency.
    private final Map<String, List<Double>> cryptoPrices = new HashMap<>();
    // A map storing the timestamps of cryptocurrency price records, where the key is the cryptocurrency symbol
    // and the value is a list of timestamps corresponding to the prices.
    private final Map<String, List<Long>> timestamps = new HashMap<>();
    
    /**
     * Loads cryptocurrency data from CSV files when the repository is first created.
     * This method is annotated with @PostConstruct to ensure it is called after the repository is initialized.
     * It reads several predefined CSV files and loads the data into memory.
     */
    @PostConstruct
    public void loadCryptoData() {
        String[] files = {"BTC_values.csv", "DOGE_values.csv", "ETH_values.csv", "LTC_values.csv", "XRP_values.csv"};
        for (String file : files) {
            loadCsvData(file);
        }
    }
    
    /**
     * Loads data from a CSV file into memory. Each line in the CSV file is expected to contain:
     * - A timestamp (long)
     * - A cryptocurrency symbol (String)
     * - A price (double)
     * 
     * The data is parsed and stored in the `cryptoPrices` and `timestamps` maps.
     * 
     * @param fileName The name of the CSV file to load data from.
     */
    private void loadCsvData(String fileName) {
        // Use classloader to load file from classpath (resources folder)
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                logger.error("File not found in resources: " + fileName);
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                br.readLine(); // Skip header
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    long timestamp = Long.parseLong(values[0]);
                    String symbol = values[1];
                    double price = Double.parseDouble(values[2]);

                    // Adds the parsed price to the appropriate cryptocurrency list
                    cryptoPrices.computeIfAbsent(symbol, k -> new ArrayList<>()).add(price);
                    timestamps.computeIfAbsent(symbol, k -> new ArrayList<>()).add(timestamp);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading data from CSV file: " + fileName, e);  // Log error with stack trace
        }
    }

    
    /**
     * Fetches the statistics for a given cryptocurrency symbol, including the minimum and maximum
     * price, as well as the timestamps of the oldest and newest data entries.
     * 
     * @param symbol The cryptocurrency symbol (e.g., "BTC", "ETH").
     * @return A map containing the statistics for the given cryptocurrency symbol.
     *         The map includes the symbol, min price, max price, oldest timestamp, and newest timestamp.
     */
    public Map<String, Object> getCryptoStats(String symbol) {
    	// Retrieves the prices and timestamps for the given cryptocurrency symbol.
    	List<Double> prices = cryptoPrices.get(symbol);
        List<Long> times = timestamps.get(symbol);
        // If no data is available for the symbol, return an empty map.
        if (prices == null || prices.isEmpty()) return Collections.emptyMap();
        // Calculate the min and max price, and the oldest and newest timestamps.
        double min = Collections.min(prices);
        double max = Collections.max(prices);
        long oldest = Collections.min(times);
        long newest = Collections.max(times);
        // Return a map containing the statistics.
        return Map.of("symbol", symbol, "min", min, "max", max, "oldest", oldest, "newest", newest);
    }

    /**
     * Fetches the list of the top cryptocurrencies based on the normalized price range.
     * The top cryptocurrencies are those with the highest price fluctuations relative to the minimum price.
     * 
     * @return A list of cryptocurrency symbols sorted by their price range (highest to lowest).
     */
    public List<String> getTopCryptos() {
        return cryptoPrices.entrySet().stream()
        		// Sorts the entries by normalized price range in descending order.
                .sorted((a, b) -> Double.compare(normalizedRange(b.getValue()), normalizedRange(a.getValue())))
             // Collects and returns the symbols of the top cryptocurrencies.
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the normalized price range for a list of cryptocurrency prices.
     * The normalized range is calculated as the difference between the max and min price,
     * divided by the min price.
     * 
     * @param prices The list of cryptocurrency prices.
     * @return The normalized price range, or 0 if the list is empty.
     */
    private double normalizedRange(List<Double> prices) {
        if (prices == null || prices.isEmpty()) return 0;
        double min = Collections.min(prices);
        double max = Collections.max(prices);
        return (max - min) / min;
    }
    
    /**
     * Fetches the cryptocurrency with the highest price range for a specific date.
     * The price range is calculated based on the price data for that particular date.
     * 
     * @param date The timestamp of the date for which to find the cryptocurrency with the highest range.
     * @return The symbol of the cryptocurrency with the highest price range on that date,
     *         or "No data available" if no data is available for that date.
     */
    public String getCryptoWithHighestRange(long date) {
        return cryptoPrices.entrySet().stream()
        		 // Filters the cryptocurrencies that have data for the given date.
        		.filter(entry -> timestamps.get(entry.getKey()).contains(date))
        		 // Finds the cryptocurrency with the highest normalized price range for that date.
        		.max(Comparator.comparingDouble(entry -> normalizedRange(entry.getValue())))
        		// Returns the symbol of the cryptocurrency with the highest range, or a default message if no data is found.
        		.map(Map.Entry::getKey)
                .orElse("No data available");
    }
}