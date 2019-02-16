package com.morgan.scraper.extractor;

import com.morgan.scraper.extractor.traverser.Traverser;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Extractor {

  protected final Logger log = LoggerFactory.getLogger(this.getClass().getName());
  protected final Traverser traverser;

  protected Extractor(final Traverser traverser) {
    this.traverser = traverser;
  }

  protected Element extractContent(final List<String> traversalPath) {
    Elements elements = traverser.getElements(traversalPath);
    if (elements.size() != 1) {
      final String msg =
          String.format(
              "expected one element per document, found %d. traversalPath: %s",
              elements.size(), traversalPath.toString());
      log.error(msg);
      throw new ExtractionException(msg);
    }

    return elements.get(0);
  }
}
