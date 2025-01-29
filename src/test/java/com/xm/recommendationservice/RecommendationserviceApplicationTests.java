package com.xm.recommendationservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.xm.recommendationservice.repository.CryptoRepository;
import com.xm.recommendationservice.service.CryptoServiceImpl;

@SpringBootTest
public class RecommendationserviceApplicationTests {

	private CryptoRepository cryptoRepository;
    private CryptoServiceImpl cryptoService;

    @BeforeEach
    public void setUp() {
        // Mocking the CryptoRepository
        cryptoRepository = Mockito.mock(CryptoRepository.class);
        cryptoService = new CryptoServiceImpl(cryptoRepository);
    }

    @Test
    public void testGetCryptoStats() {
        // Prepare mock data
        String symbol = "BTC";
        
        // Set up mock behavior
        Mockito.when(cryptoRepository.getCryptoStats(symbol)).thenReturn(
            Map.of("symbol", symbol, "min", 34000.0, "max", 36000.0, "oldest", 1617183600L, "newest", 1617190800L)
        );
        
        // Call the method to test
        Map<String, Object> stats = cryptoService.getCryptoStats(symbol);
        
        // Assert the results
        assertNotNull(stats);
        assertEquals(34000.0, stats.get("min"));
        assertEquals(36000.0, stats.get("max"));
        assertEquals(1617183600L, stats.get("oldest"));
        assertEquals(1617190800L, stats.get("newest"));
    }

    @Test
    public void testGetTopCryptos() {
        // Prepare mock data
        Map<String, List<Double>> mockData = new HashMap<>();
        mockData.put("BTC", Arrays.asList(35000.0, 36000.0, 34000.0));
        mockData.put("ETH", Arrays.asList(2000.0, 2200.0, 2100.0));
        
        // Set up mock behavior
        Mockito.when(cryptoRepository.getTopCryptos()).thenReturn(Arrays.asList("BTC", "ETH"));
        
        // Call the method to test
        List<String> topCryptos = cryptoService.getTopCryptos();
        
        // Assert the results
        assertNotNull(topCryptos);
        assertEquals(2, topCryptos.size());
        assertEquals("BTC", topCryptos.get(0));
        assertEquals("ETH", topCryptos.get(1));
    }

    @Test
    public void testGetCryptoWithHighestRange() {
        // Prepare mock data
        long date = 1617187200L;  // some timestamp
        Map<String, List<Double>> mockData = new HashMap<>();
        mockData.put("BTC", Arrays.asList(35000.0, 36000.0, 34000.0));
        mockData.put("ETH", Arrays.asList(2000.0, 2200.0, 2100.0));
        
        // Set up mock behavior
        Mockito.when(cryptoRepository.getCryptoWithHighestRange(date)).thenReturn("BTC");
        
        // Call the method to test
        String cryptoWithHighestRange = cryptoService.getCryptoWithHighestRange(date);
        
        // Assert the results
        assertNotNull(cryptoWithHighestRange);
        assertEquals("BTC", cryptoWithHighestRange);
    }

}
