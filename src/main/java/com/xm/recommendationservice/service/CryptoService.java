package com.xm.recommendationservice.service;

import java.util.List;
import java.util.Map;

public interface CryptoService {
	    Map<String, Object> getCryptoStats(String symbol);
	    List<String> getTopCryptos();
	    String getCryptoWithHighestRange(long date);
	}

