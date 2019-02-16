package com.morgan.scraper.extractor.productpage;

import java.math.BigDecimal;

public interface ProductPageExtractor {
  BigDecimal getPricePerUnit();

  String getDescription();

  Integer getKcalPer100g();

  String getTitle();
}
