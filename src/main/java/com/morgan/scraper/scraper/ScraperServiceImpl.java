package com.morgan.scraper.scraper;

import com.morgan.scraper.extractor.productpage.JsoupProductPageExtractor;
import com.morgan.scraper.extractor.rootpage.RootPageExtractor;
import com.morgan.scraper.extractor.traverser.JsoupTraverser;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScraperServiceImpl implements ScraperService {

  private final RootPageExtractor rootPageExtractor;

  @Autowired
  public ScraperServiceImpl(final RootPageExtractor rootPageExtractor) {
    this.rootPageExtractor = rootPageExtractor;
  }

  @Override
  public List<ScrapedProduct> getScrapedProductList() {
    return rootPageExtractor
        .getProductPageUrls()
        .stream()
        .map(p -> new JsoupTraverser(p.toString()))
        .map(JsoupProductPageExtractor::new)
        .map(
            e -> {
              final String title = e.getTitle();
              final String description = e.getDescription();
              final BigDecimal pricePerUnit = e.getPricePerUnit();
              final Integer kcalPer100g = e.getKcalPer100g();
              return new ScrapedProduct(title, description, kcalPer100g, pricePerUnit);
            })
        .collect(Collectors.toList());
  }
}
