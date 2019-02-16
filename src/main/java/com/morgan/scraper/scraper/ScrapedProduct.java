package com.morgan.scraper.scraper;

import java.math.BigDecimal;
import java.util.Objects;

public class ScrapedProduct {

  private final String title;
  private final String description;
  private final Integer kcalPer100g;
  private final BigDecimal price;

  ScrapedProduct(String title, String description, Integer kcalPer100g, BigDecimal price) {
    this.title = title;
    this.description = description;
    this.kcalPer100g = kcalPer100g;
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScrapedProduct that = (ScrapedProduct) o;
    return Objects.equals(title, that.title)
        && Objects.equals(description, that.description)
        && Objects.equals(kcalPer100g, that.kcalPer100g)
        && Objects.equals(price, that.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, kcalPer100g, price);
  }
}
