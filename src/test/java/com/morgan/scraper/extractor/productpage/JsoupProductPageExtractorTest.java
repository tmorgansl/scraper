package com.morgan.scraper.extractor.productpage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.morgan.scraper.extractor.ExtractionException;
import java.math.BigDecimal;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupProductPageExtractorTest {

  @Test
  public void testProductTitleExtractedProperly() {
    final String html =
        "<body><div class='productTitleDescriptionContainer'><h1>mock product title</h1></div></body>";
    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    final String title = productPageExtractor.getTitle();

    final String expectedTitle = "mock product title";
    assertEquals(expectedTitle, title);
  }

  @Test(expected = ExtractionException.class)
  public void testProductMultipleTitlesThrowsExtractionException() {
    final String html =
        "<body><div class='productTitleDescriptionContainer'><h1>mock product title<h1><h1>second mock title</h1></div></body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    productPageExtractor.getTitle();
  }

  @Test
  public void testGetPricePerUnitExtractedProperly() {
    final String html =
        "<body><div class='productSummary'><div><div class='pricePerUnit'>£1.23</div></div></div></body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    final BigDecimal price = productPageExtractor.getPricePerUnit();

    final BigDecimal expectedPrice = new BigDecimal("1.23");
    assertEquals(expectedPrice, price);
  }

  @Test(expected = ExtractionException.class)
  public void testProductMultiplePricesThrowsExtractionException() {
    final String html =
        "<body><div class='productSummary'><div><div class='pricePerUnit'>£1.23</div><div class='pricePerUnit'>£2.34</div></div></div></body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    productPageExtractor.getPricePerUnit();
  }

  @Test
  public void testGetDescriptionPullsFirstLineOnly() {
    final String html =
        "<body>"
            + "<div>"
            + "<h3 class='description'>Description</h3>"
            + "<div><p></p><p>first line</p><p>second line</p></div>"
            + "</div>"
            + "</body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    final String description = productPageExtractor.getDescription();

    final String expectedDescription = "first line";

    assertEquals(expectedDescription, description);
  }

  @Test(expected = ExtractionException.class)
  public void testDescriptionMultipleDescriptionHeadersThrowsExtractionException() {
    final String html =
        "<body>"
            + "<div>"
            + "<h3 class='description'>Description</h3>"
            + "<h3 class='description'>Description2</h3>"
            + "<div><p></p><p>first line</p><p>second line</p></div>"
            + "</div>"
            + "</body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    productPageExtractor.getDescription();
  }

  @Test
  public void testGetKcalByFirstPath() {
    final String html = "<body><div><table><th>Energy kcal</th><th>123</th></table></div></body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    final Integer kcalPer100g = productPageExtractor.getKcalPer100g();

    assertEquals(Integer.valueOf(123), kcalPer100g);
  }

  @Test
  public void testGetKcalBySecondPath() {
    final String html =
        "<body>"
            + " <table class='nutritionTable'>"
            + "<thead> "
            + "<tr class='tableTitleRow'>"
            + "<th scope='col'></th><th scope='col'>Per 100g&nbsp;</th><th scope='col'>% based on RI for Average Adult</th> "
            + "</tr> "
            + "</thead>"
            + "<tr class='tableRow1'>"
            + "<th scope='row' class='rowHeader' rowspan='2'>Energy</th><td class='tableRow1'>133kJ</td><td class='tableRow1'>-</td>"
            + "</tr> "
            + "<tr class='tableRow0'> "
            + "<td class='tableRow0'>32kcal</td><td class='tableRow0'>2%</td>"
            + "</tr> "
            + "</table>"
            + "</body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    final Integer kcalPer100g = productPageExtractor.getKcalPer100g();

    assertEquals(Integer.valueOf(32), kcalPer100g);
  }

  @Test
  public void testGetKcalNoHeadersFoundReturnsNull() {
    final String html = "<body><div></div></body>";

    final ProductPageExtractor productPageExtractor =
        new JsoupProductPageExtractor(Jsoup.parse(html));

    final Integer kcalPer100g = productPageExtractor.getKcalPer100g();

    assertNull(kcalPer100g);
  }
}
