package com.morgan.scraper.traverser;

import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsoupTraverser implements TraverserService {

  private final Document document;

  @Autowired
  JsoupTraverser(final Document document) {
    this.document = document;
  }

  @Override
  public List<DOMElement> getElements(final List<String> queries) {
    Elements elements = document.body().children();
    for (String query : queries) {
      elements = elements.select(query);
    }
    return elements
        .stream()
        .map(e -> new DOMElement(e.attr(constants.HREF)))
        .collect(Collectors.toList());
  }
}
