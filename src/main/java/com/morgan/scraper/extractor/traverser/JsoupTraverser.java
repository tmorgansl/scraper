package com.morgan.scraper.extractor.traverser;

import com.morgan.scraper.extractor.ExtractionException;
import java.io.IOException;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupTraverser implements Traverser {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final Document document;

  public JsoupTraverser(final String url) {
    try {
      this.document = Jsoup.connect(url).get();
    } catch (IOException e) {
      final String msg = String.format("cannot connect to url '%s'", url);
      log.error(msg);
      throw new ExtractionException(msg, e);
    }
  }

  public JsoupTraverser(final Document document) {
    this.document = document;
  }

  @Override
  public Elements getElements(final List<String> queries) {
    Elements elements = document.body().children();
    for (String query : queries) {
      elements = elements.select(query);
    }

    return elements;
  }
}
