package com.morgan.scraper.extractor.traverser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupTraverserTest {

  private static JsoupTraverser jsoupTraverser;
  private static Document document;

  @Before
  public void init() {
    final String html =
        "<body>"
            + "<div class='children' href='../testoutside.html' id='outside'></div>"
            + "<div class='container'>"
            + "<div class='children' href='../test1.html' id='1'></div>"
            + "<div class='children' href='../test2.html' id='2'></div>"
            + "</div>"
            + "</body>";

    document = Jsoup.parse(html);
    jsoupTraverser = new JsoupTraverser(document);
  }

  @Test
  public void testMatchingQueryWithContainerReturnsCorrectElements() {
    List<String> cssQueries = Arrays.asList(".container", ".children");

    Elements elements = jsoupTraverser.getElements(cssQueries);

    Elements expectedElements = document.select("#1,#2");
    assertEquals(expectedElements, elements);
  }

  @Test
  public void testMatchingQueryWithoutContainerReturnsCorrectElements() {
    List<String> cssQueries = Collections.singletonList(".children");

    Elements elements = jsoupTraverser.getElements(cssQueries);

    Elements expectedElements = document.select("#outside,#1,#2");
    assertEquals(expectedElements, elements);
  }

  @Test
  public void testNonMatchingQueryReturnsEmptyList() {
    List<String> cssQueries = Arrays.asList(".container", ".something-missing");

    Elements elements = jsoupTraverser.getElements(cssQueries);

    assertEquals(0, elements.size());
  }
}
