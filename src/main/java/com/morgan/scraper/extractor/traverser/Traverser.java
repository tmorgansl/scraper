package com.morgan.scraper.extractor.traverser;

import java.util.List;
import org.jsoup.select.Elements;

public interface Traverser {
  Elements getElements(List<String> queries);
}
