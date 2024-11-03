package com.stock.portfolio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.portfolio.model.Stock;
import com.stock.portfolio.repository.StockPortfolioRepository;

@Service
public class StockPortfolioService {
	
	@Autowired
	private StockPortfolioRepository portfolioRepository;
	
	private static final String PUBLIC_API_KEY = "QDVJTDME1C560DA1";
	
	private static final String PUBLIC_API_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey="+PUBLIC_API_KEY;
	
	public Stock addStock(Stock stock) {
		BigDecimal currentPrice = getPrice(stock.getSymbol());
		BigDecimal totalValue = currentPrice.multiply(BigDecimal.valueOf(stock.getQuantity()));
		
		stock.setCurrentPrice(currentPrice);
		stock.setTotalValue(totalValue);
		return portfolioRepository.save(stock);
	}
	
	public void removeStock(String symbol) {
		portfolioRepository.findBySymbol(symbol).ifPresent(portfolioRepository::delete);
	}
	
	public List<Stock> getAllStocks(){
		return portfolioRepository.findAll();
	}
	
	public BigDecimal getTotalValuePortfolio() {
	    return portfolioRepository.findAll().stream()
	        .map(stock -> {
	            BigDecimal currentPrice = getPrice(stock.getSymbol());
	            return currentPrice != null ? currentPrice.multiply(BigDecimal.valueOf(stock.getQuantity())) : BigDecimal.ZERO;
	        })
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public BigDecimal getPrice(String symbol) {
	    RestTemplate restTemplate = new RestTemplate();
	    String url = PUBLIC_API_URL.replace("{symbol}", symbol);

	    return Optional.ofNullable(restTemplate.getForObject(url, String.class))
	        .map(response -> {
	            try {
	                JsonNode rootObject = new ObjectMapper().readTree(response);
	                return rootObject.path("Global Quote").path("05. price").asText();
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	                return null;
	            }
	        })
	        .filter(price -> price != null && !price.isEmpty())
	        .map(BigDecimal::new)
	        .orElse(null);
	}


}