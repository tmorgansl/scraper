package com.morgan.scraper.extractor.productpage;

import com.morgan.scraper.extractor.ExtractionException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupProductPageExtractor implements ProductPageExtractor {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final Document document;

  private final List<String> titleTraversalPath =
      Arrays.asList(".productTitleDescriptionContainer", "h1");
  private final List<String> priceTraversalPath = Arrays.asList(".productSummary", ".pricePerUnit");
  private final List<String> kcalPer100gFirstTraversalPath =
      Collections.singletonList("th:contains(Energy kcal)");
  private final List<String> kcalPer100gSecondTraversalPath =
      Collections.singletonList("th:contains(Energy)");
  private final List<String> descriptionTraversalPath =
      Collections.singletonList("h3:contains(Description)");

  public JsoupProductPageExtractor(final URL url) {
    try {
      this.document = Jsoup.connect(url.toString()).get();
    } catch (IOException e) {
      final String msg = String.format("cannot connect to url %s", url);
      throw new ExtractionException(msg, e);
    }
  }

  JsoupProductPageExtractor(final Document document) {
    this.document = document;
  }

  @Override
  public BigDecimal getPricePerUnit() {
    String pricePerUnitRaw = extractSingleElement(priceTraversalPath).ownText();
    // remove currency
    String pricePerUnitDigits = pricePerUnitRaw.substring(1);
    return new BigDecimal(pricePerUnitDigits);
  }

  @Override
  public String getDescription() {
    return extractSingleElement(descriptionTraversalPath)
        .nextElementSibling()
        .select("p")
        .stream()
        .filter(p -> !p.ownText().isEmpty())
        .collect(Collectors.toList())
        .get(0)
        .ownText();
  }

  @Override
  public Integer getKcalPer100g() {
    String kcalPer100g = null;
    Elements kcalElements = extractElements(kcalPer100gFirstTraversalPath);
    if (kcalElements.size() == 1) {
      kcalPer100g = kcalElements.get(0).nextElementSibling().ownText();
    } else {
      kcalElements = extractElements(kcalPer100gSecondTraversalPath);
      if (kcalElements.size() == 1) {
        final String rawKcalPer100g =
            kcalElements
                .get(0)
                .parent()
                .nextElementSibling()
                .select(".tableRow0")
                .select("td")
                .first()
                .ownText();
        kcalPer100g = rawKcalPer100g.replaceAll("[^0-9]", "");
      }
    }
    return (Objects.isNull(kcalPer100g)) ? null : Integer.parseInt(kcalPer100g);
  }

  @Override
  public String getTitle() {
    return extractSingleElement(titleTraversalPath).ownText();
  }

  private Element extractSingleElement(final List<String> queries) {

    final Elements elements = extractElements(queries);

    if (elements.size() != 1) {
      final String msg =
          String.format(
              "found %d elements for queries %s, expected 1", elements.size(), queries.toString());
      log.error(msg);
      throw new ExtractionException(msg);
    }

    return elements.get(0);
  }

  private Elements extractElements(final List<String> queries) {
    Elements elements = document.body().children();

    for (String q : queries) {
      elements = elements.select(q);
    }

    return elements;
  }
}
