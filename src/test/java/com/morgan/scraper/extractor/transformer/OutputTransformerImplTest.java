package com.morgan.scraper.extractor.transformer;

import static junit.framework.TestCase.assertEquals;

import com.morgan.scraper.scraper.ScrapedProduct;
import com.morgan.scraper.transformer.Output;
import com.morgan.scraper.transformer.OutputTransformerImpl;
import com.morgan.scraper.transformer.ProductOutput;
import com.morgan.scraper.transformer.TotalOutput;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutputTransformerImplTest {
  private static OutputTransformerImpl productTransformer;
  private static final BigDecimal vatRate = new BigDecimal("0.23");

  @Before
  public void init() {
    productTransformer = new OutputTransformerImpl(vatRate);
  }

  @Test
  public void testScrapedProductsTransformedCorrectly() {

    List<ScrapedProduct> scrapedProducts =
        Arrays.asList(
            new ScrapedProduct(
                "product title 1", "product description 1", 3, new BigDecimal("1.23")),
            new ScrapedProduct(
                "product title 2", "product description 2", 4, new BigDecimal("2.34")),
            new ScrapedProduct(
                "product title 3", "product description 3", null, new BigDecimal("3.43")));

    final List<ProductOutput> expectedProducts = getExpectedProducts(scrapedProducts);

    final TotalOutput expectedTotal =
        new TotalOutput(new BigDecimal("7.00"), new BigDecimal("1.31"));
    final Output expectedOutput = new Output(expectedProducts, expectedTotal);

    final Output output = productTransformer.getOutput(scrapedProducts);

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testScrapedProductVATRoundUpCorrectly() {
    List<ScrapedProduct> scrapedProducts =
        Arrays.asList(
            new ScrapedProduct(
                "product title 1", "product description 1", 3, new BigDecimal("1.23")),
            new ScrapedProduct(
                "product title 2", "product description 2", 4, new BigDecimal("2.34")),
            new ScrapedProduct(
                "product title 3", "product description 3", null, new BigDecimal("3.53")));

    final List<ProductOutput> expectedProducts = getExpectedProducts(scrapedProducts);

    final TotalOutput expectedTotal =
        new TotalOutput(new BigDecimal("7.10"), new BigDecimal("1.33"));
    final Output expectedOutput = new Output(expectedProducts, expectedTotal);

    final Output output = productTransformer.getOutput(scrapedProducts);

    assertEquals(expectedOutput, output);
  }

  private List<ProductOutput> getExpectedProducts(List<ScrapedProduct> scrapedProducts) {
    return scrapedProducts
        .stream()
        .map(
            s ->
                new ProductOutput(
                    s.getTitle(), s.getDescription(), s.getKcalPer100g(), s.getPrice()))
        .collect(Collectors.toList());
  }
}
