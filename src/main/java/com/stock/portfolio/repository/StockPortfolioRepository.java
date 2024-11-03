package com.stock.portfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stock.portfolio.model.Stock;

public interface StockPortfolioRepository extends JpaRepository<Stock,Long>{

	Optional<Stock> findBySymbol(String symbol);
}
