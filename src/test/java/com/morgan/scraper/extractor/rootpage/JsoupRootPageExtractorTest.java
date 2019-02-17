package com.morgan.scraper.extractor.rootpage;

import static junit.framework.TestCase.assertEquals;

import com.morgan.scraper.extractor.ExtractionException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupRootPageExtractorTest {

  private static final String url = "http://www.test-site.com/path/index.html";
  private static Document document;

  @Before
  public void init() {
    final String html =
        "<body><div class='productNameAndPromotions'><a class='test' href='../relative1.html'></a>"
            + "<a class='test' href='../relative2.html'></a>"
            + "<a class='test' href='../relative3.html'></a></div></body>";

    document = Jsoup.parse(html);
  }

  @Test
  public void testGetProductPageUrlsReturnsCorrectResponse() throws MalformedURLException {
    final RootPageExtractor rootPageExtractor = new JsoupRootPageExtractor(document, url);

    List<URL> urls = rootPageExtractor.getProductPageUrls();

    final List<URL> expectedURLs = new ArrayList<>();
    for (String s :
        Arrays.asList(
            "http://www.test-site.com/relative1.html",
            "http://www.test-site.com/relative2.html",
            "http://www.test-site.com/relative3.html")) {
      URL url1 = new URL(s);
      expectedURLs.add(url1);
    }

    assertEquals(expectedURLs, urls);
  }

  @Test(expected = ExtractionException.class)
  public void testMalformedURLThrowsExtractionException() {
    new JsoupRootPageExtractor(document, ".some.bad.url");
  }
}
