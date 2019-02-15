package com.morgan.scraper.traverser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
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
public class JsoupTraverserTest {

  private static JsoupTraverser jsoupTraverser;

  @Before
  public void init() {
    final String html =
        "<body>"
            + "<div class='children' href='../testoutside.html'></div>"
            + "<div class='container'>"
            + "<div class='children' href='../test1.html'></div>"
            + "<div class='children' href='../test2.html'></div>"
            + "</div>"
            + "</body>";

    final Document document = Jsoup.parse(html);
    jsoupTraverser = new JsoupTraverser(document);
  }

  @Test
  public void testMatchingQueryWithContainerReturnsCorrectElements() {
    List<String> cssQueries = Arrays.asList(".container", ".children");

    List<DOMElement> elements = jsoupTraverser.getElements(cssQueries);

    List<DOMElement> expectedElements =
        Arrays.asList(new DOMElement("../test1.html"), new DOMElement("../test2.html"));
    assertEquals(expectedElements, elements);
  }

  @Test
  public void testMatchingQueryWithoutContainerReturnsCorrectElements() {
    List<String> cssQueries = Collections.singletonList(".children");

    List<DOMElement> elements = jsoupTraverser.getElements(cssQueries);

    List<DOMElement> expectedElements =
        Arrays.asList(
            new DOMElement("../testoutside.html"),
            new DOMElement("../test1.html"),
            new DOMElement("../test2.html"));
    assertEquals(expectedElements, elements);
  }

  @Test
  public void testNonMatchingQueryReturnsEmptyList() {
    List<String> cssQueries = Arrays.asList(".container", ".something-missing");

    List<DOMElement> elements = jsoupTraverser.getElements(cssQueries);

    assertEquals(0, elements.size());
  }
}
