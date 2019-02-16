package com.morgan.scraper.extractor.rootpage;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

import com.morgan.scraper.extractor.ExtractionException;
import com.morgan.scraper.extractor.traverser.Traverser;
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
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupRootPageExtractorTest {

  private static Traverser traverser;
  private static final String url = "http://www.test-site.com/path/index.html";
  private static JsoupRootPageExtractor jsoupRootPageExtractor;

  @Before
  public void init() {
    traverser = Mockito.mock(Traverser.class);
    jsoupRootPageExtractor = new JsoupRootPageExtractor(traverser, url);
  }

  @Test
  public void testGetProductPageUrlsReturnsCorrectResponse() throws MalformedURLException {
    final String html =
        "<body><div class='test' href='../relative1.html'></div>"
            + "<div class='test' href='../relative2.html'></div>"
            + "<div class='test' href='../relative3.html'></div></body>";

    final Document document = Jsoup.parse(html);

    when(traverser.getElements(JsoupRootPageExtractor.rootTraversalPath))
        .thenReturn(document.body().children());

    List<URL> urls = jsoupRootPageExtractor.getProductPageUrls();

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
    jsoupRootPageExtractor = new JsoupRootPageExtractor(traverser, ".some.bad.url");
  }
}
