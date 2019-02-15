package com.morgan.scraper;

import com.morgan.scraper.traverser.DOMElement;
import com.morgan.scraper.traverser.TraverserService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScraperApplication implements CommandLineRunner {

  @Autowired private TraverserService traverserService;

  @Bean
  public Document getRootDocument(@Value("${sainsburys.baseURL}") String baseURL)
      throws IOException {
    return Jsoup.connect(baseURL).get();
  }

  @Override
  public void run(String... args) {
    List<String> traversalPath = Arrays.asList(".productNameAndPromotions", "a");
    List<DOMElement> domElements = traverserService.getElements(traversalPath);
    for (DOMElement e : domElements) {
      System.out.println(e.getHref());
    }
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ScraperApplication.class);
    app.setLogStartupInfo(false);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }
}
