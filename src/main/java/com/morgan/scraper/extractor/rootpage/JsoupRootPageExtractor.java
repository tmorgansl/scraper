package com.morgan.scraper.extractor.rootpage;

import com.morgan.scraper.extractor.ExtractionException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JsoupRootPageExtractor implements RootPageExtractor {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final URL url;
  private final Document rootDocument;

  @Autowired
  JsoupRootPageExtractor(Document rootDocument, @Value("${sainsburys.url}") String baseURL) {
    this.rootDocument = rootDocument;
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
    final Elements elements = rootDocument.select(".productNameAndPromotions").select("a");
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
