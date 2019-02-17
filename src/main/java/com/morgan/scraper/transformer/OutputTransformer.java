package com.morgan.scraper.transformer;

import com.morgan.scraper.scraper.ScrapedProduct;
import java.util.List;

public interface OutputTransformer {
  Output getOutput(List<ScrapedProduct> scrapedProductList);
}
