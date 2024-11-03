package com.stock.portfolio.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stock.portfolio.model.Stock;
import com.stock.portfolio.service.StockPortfolioService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/stock")
public class StockPortfolioController {
	
	@Autowired
	private StockPortfolioService stockPortfolioService;
	
	@PostMapping
	public Stock addStock(@RequestBody Stock stock) {
		return stockPortfolioService.addStock(stock);
	}
	
	@DeleteMapping("/{symbol}")
	public void deleteStock(@PathVariable String symbol) {
		stockPortfolioService.removeStock(symbol);
	}
	
	@GetMapping
	public List<Stock> getAllStocks(){
		return stockPortfolioService.getAllStocks();
	}
	
	@GetMapping("/totalValue")
	public BigDecimal getTotalPortfolioValue() {
		return stockPortfolioService.getTotalValuePortfolio();
	}
}
