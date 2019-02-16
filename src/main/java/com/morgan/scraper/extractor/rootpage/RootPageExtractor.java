package com.morgan.scraper.extractor.rootpage;

import java.net.URL;
import java.util.List;

public interface RootPageExtractor {
  List<URL> getProductPageUrls();
}
