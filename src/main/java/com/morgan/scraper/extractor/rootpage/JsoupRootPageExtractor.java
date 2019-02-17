package com.morgan.scraper.extractor.rootpage;

import com.morgan.scraper.extractor.ExtractionException;
import com.morgan.scraper.extractor.Extractor;
import com.morgan.scraper.extractor.traverser.Traverser;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JsoupRootPageExtractor extends Extractor implements RootPageExtractor {

  private final URL url;

  static final List<String> rootTraversalPath = Arrays.asList(".productNameAndPromotions", "a");

  @Autowired
  JsoupRootPageExtractor(Traverser traverser, @Value("${sainsburys.url}") String baseURL) {
    super(traverser);
    try {
      this.url = new URL(baseURL);
    } catch (MalformedURLException e) {
      final String msg = String.format("cannot parse '%s' as a valid url", baseURL);
      log.error(msg);
      throw new ExtractionException(msg, e);
    }
  }

  @Override
  public List<URL> getProductPageUrls() {
    final Elements elements = traverser.getElements(rootTraversalPath);
    List<URL> urls = new ArrayList<>();
    for (Element element : elements) {
      try {
        URL url = new URL(this.url, element.attr(Constants.HREF));
        urls.add(url);
      } catch (MalformedURLException e) {
        final String msg =
            String.format(
                "relative path '%s' could not be joined to base URL to form a valid url",
                element.attr(Constants.HREF));
        log.error(msg);
        throw new ExtractionException(msg, e);
      }
    }
    return urls;
  }
}
