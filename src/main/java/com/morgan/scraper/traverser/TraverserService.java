package com.morgan.scraper.traverser;

import java.util.List;

public interface TraverserService {
  List<DOMElement> getElements(List<String> queries);
}
