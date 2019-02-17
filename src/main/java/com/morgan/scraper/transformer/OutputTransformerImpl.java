package com.morgan.scraper.transformer;

import com.morgan.scraper.scraper.ScrapedProduct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OutputTransformerImpl implements OutputTransformer {

  private final BigDecimal vatRate;
  private final BigDecimal one = new BigDecimal("1");
  private final RoundingMode roundingMode = RoundingMode.HALF_UP;

  @Autowired
  public OutputTransformerImpl(@Value("${tax.vat}") BigDecimal vatRate) {
    this.vatRate = vatRate;
  }

  @Override
  public Output getOutput(final List<ScrapedProduct> scrapedProductList) {

    BigDecimal gross = new BigDecimal("0");
    List<ProductOutput> productOutputs = new ArrayList<>();

    for (ScrapedProduct p : scrapedProductList) {
      final ProductOutput product =
          new ProductOutput(p.getTitle(), p.getDescription(), p.getKcalPer100g(), p.getPrice());
      gross = gross.add(p.getPrice());
      productOutputs.add(product);
    }

    BigDecimal vat = gross.multiply(vatRate).divide(one.add(vatRate), roundingMode);

    gross = gross.setScale(2, roundingMode);
    vat = vat.setScale(2, roundingMode);

    final TotalOutput total = new TotalOutput(gross, vat);

    return new Output(productOutputs, total);
  }
}
