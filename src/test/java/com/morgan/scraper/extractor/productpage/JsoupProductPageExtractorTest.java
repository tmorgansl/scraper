package com.morgan.scraper.extractor.productpage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.morgan.scraper.extractor.ExtractionException;
import com.morgan.scraper.extractor.traverser.Traverser;
import java.math.BigDecimal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupProductPageExtractorTest {
  private static Traverser traverser;
  private static JsoupProductPageExtractor jsoupProductPageExtractor;

  @Before
  public void init() {
    traverser = Mockito.mock(Traverser.class);
    jsoupProductPageExtractor = new JsoupProductPageExtractor(traverser);
  }

  @Test
  public void testProductTitleExtractedProperly() {
    final String html = "<body><div>mock product title</div></body>";

    final Elements mockElements = buildMockElements(html);

    when(traverser.getElements(JsoupProductPageExtractor.titleTraversalPath))
        .thenReturn(mockElements);

    final String title = jsoupProductPageExtractor.getTitle();

    final String expectedTitle = "mock product title";

    assertEquals(expectedTitle, title);
  }

  @Test(expected = ExtractionException.class)
  public void testProductMultipleTitlesThrowsExtractionException() {
    final String html = "<body><div>mock product title</div><div>second mock title</div></body>";

    final Elements mockElements = buildMockElements(html);

    when(traverser.getElements(JsoupProductPageExtractor.titleTraversalPath))
        .thenReturn(mockElements);

    jsoupProductPageExtractor.getTitle();
  }

  @Test
  public void testGetPricePerUnitExtractedProperly() {
    final String html = "<body><div>£1.23</div></body>";

    final Elements mockElements = buildMockElements(html);

    when(traverser.getElements(JsoupProductPageExtractor.priceTraversalPath))
        .thenReturn(mockElements);

    final BigDecimal price = jsoupProductPageExtractor.getPricePerUnit();

    final BigDecimal expectedPrice = new BigDecimal("1.23");
    assertEquals(expectedPrice, price);
  }

  @Test(expected = ExtractionException.class)
  public void testProductMultiplePricesThrowsExtractionException() {
    final String html = "<body><div>£1.23</div><div>£2.34</div></body>";

    final Elements mockElements = buildMockElements(html);

    when(traverser.getElements(JsoupProductPageExtractor.priceTraversalPath))
        .thenReturn(mockElements);

    jsoupProductPageExtractor.getPricePerUnit();
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

    final Elements mockElements = buildMockElements(html);
    final Elements inputElements = mockElements.select(".description");

    when(traverser.getElements(JsoupProductPageExtractor.descriptionTraversalPath))
        .thenReturn(inputElements);

    final String description = jsoupProductPageExtractor.getDescription();

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

    final Elements mockElements = buildMockElements(html);
    final Elements inputElements = mockElements.select(".description");

    when(traverser.getElements(JsoupProductPageExtractor.descriptionTraversalPath))
        .thenReturn(inputElements);

    jsoupProductPageExtractor.getDescription();
  }

  @Test
  public void testGetKcalByFirstPath() {
    final String html =
        "<body>"
            + "<div>"
            + "<h3 class='energy'>Energy</h3>"
            + "<div>123</div>"
            + "</div>"
            + "</body>";

    final Elements mockElements = buildMockElements(html);
    final Elements inputElements = mockElements.select(".energy");

    when(traverser.getElements(JsoupProductPageExtractor.kcalPer100gFirstTraversalPath))
        .thenReturn(inputElements);

    final Integer kcalPer100g = jsoupProductPageExtractor.getKcalPer100g();

    assertEquals(Integer.valueOf(123), kcalPer100g);
  }

  @Test
  public void testGetKcalBySecondPath() {
    final String html =
        "<body>"
            + "<div>"
            + "<h3 class='energy'>Energy</h3>"
            + "</div>"
            + "<table>"
            + "<tr class='tableRow0'>"
            + "<td>123kcal</td><td>345kcal</td>"
            + "</tr>"
            + "</table>"
            + "</body>";

    final Elements mockElements = buildMockElements(html);
    final Elements inputElementsFirst = mockElements.select(".empty");
    final Elements inputElementsSecond = mockElements.select(".energy");

    when(traverser.getElements(JsoupProductPageExtractor.kcalPer100gFirstTraversalPath))
        .thenReturn(inputElementsFirst);
    when(traverser.getElements(JsoupProductPageExtractor.kcalPer100gSecondTraversalPath))
        .thenReturn(inputElementsSecond);

    final Integer kcalPer100g = jsoupProductPageExtractor.getKcalPer100g();

    assertEquals(Integer.valueOf(123), kcalPer100g);
  }

  @Test
  public void testGetKcalNoHeadersFoundReturnsNull() {
    final String html = "<body>" + "<div></div>" + "</body>";

    final Elements mockElements = buildMockElements(html);

    final Elements inputElements = mockElements.select(".empty");

    when(traverser.getElements(JsoupProductPageExtractor.kcalPer100gFirstTraversalPath))
        .thenReturn(inputElements);
    when(traverser.getElements(JsoupProductPageExtractor.kcalPer100gSecondTraversalPath))
        .thenReturn(inputElements);

    final Integer kcalPer100g = jsoupProductPageExtractor.getKcalPer100g();

    assertNull(kcalPer100g);
  }

  private Elements buildMockElements(final String html) {
    Document document = Jsoup.parse(html);
    return document.body().children();
  }
}
