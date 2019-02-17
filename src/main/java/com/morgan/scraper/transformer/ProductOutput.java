package com.morgan.scraper.transformer;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductOutput {
  private final String title;
  private final String description;

  @SerializedName("kcal_per_100g")
  private final Integer kcalPer100g;

  @SerializedName("unit_price")
  private final BigDecimal price;

  public ProductOutput(String title, String description, Integer kcalPer100g, BigDecimal price) {
    this.title = title;
    this.description = description;
    this.kcalPer100g = kcalPer100g;
    this.price = price;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public Integer getKcalPer100g() {
    return kcalPer100g;
  }

  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductOutput that = (ProductOutput) o;
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
