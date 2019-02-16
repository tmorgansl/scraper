package com.morgan.scraper.extractor.productpage;

import com.morgan.scraper.extractor.Extractor;
import com.morgan.scraper.extractor.traverser.Traverser;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jsoup.select.Elements;

public class JsoupProductPageExtractor extends Extractor implements ProductPageExtractor {

  static final List<String> titleTraversalPath =
      Arrays.asList(".productTitleDescriptionContainer", "h1");
  static final List<String> priceTraversalPath = Arrays.asList(".productSummary", ".pricePerUnit");
  static final List<String> kcalPer100gFirstTraversalPath =
      Collections.singletonList("th:contains(Energy kcal)");
  static final List<String> kcalPer100gSecondTraversalPath =
      Collections.singletonList("th:contains(Energy)");
  static final List<String> descriptionTraversalPath =
      Collections.singletonList("h3:contains(Description)");

  public JsoupProductPageExtractor(final Traverser traverser) {
    super(traverser);
  }

  @Override
  public BigDecimal getPricePerUnit() {
    String pricePerUnitRaw = extractContent(priceTraversalPath).ownText();
    // remove currency
    String pricePerUnitDigits = pricePerUnitRaw.substring(1);
    return new BigDecimal(pricePerUnitDigits);
  }

  @Override
  public String getDescription() {
    return extractContent(descriptionTraversalPath)
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
    Elements kcalElements = this.traverser.getElements(kcalPer100gFirstTraversalPath);
    if (kcalElements.size() == 1) {
      kcalPer100g = kcalElements.get(0).nextElementSibling().ownText();
    } else {
      kcalElements = traverser.getElements(kcalPer100gSecondTraversalPath);
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
    return extractContent(titleTraversalPath).ownText();
  }
}
