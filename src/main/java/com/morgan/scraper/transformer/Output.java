package com.morgan.scraper.transformer;

import java.util.List;
import java.util.Objects;

public class Output {
  private final List<ProductOutput> results;
  private final TotalOutput total;

  public Output(List<ProductOutput> results, TotalOutput total) {
    this.results = results;
    this.total = total;
  }

  public List<ProductOutput> getResults() {
    return results;
  }

  public TotalOutput getTotal() {
    return total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Output output = (Output) o;
    return Objects.equals(results, output.results) && Objects.equals(total, output.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(results, total);
  }
}
