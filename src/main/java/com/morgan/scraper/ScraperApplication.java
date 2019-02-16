package com.morgan.scraper;

import com.morgan.scraper.extractor.traverser.JsoupTraverser;
import com.morgan.scraper.extractor.traverser.Traverser;
import com.morgan.scraper.scraper.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScraperApplication implements CommandLineRunner {

  @Autowired private ScraperService scraperService;

  @Bean
  public Traverser getRootTraverser(@Value("${sainsburys.baseURL}") String baseURL) {
    return new JsoupTraverser(baseURL);
  }

  @Override
  public void run(String... args) {
    scraperService.getScrapedProductList();
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ScraperApplication.class);
    app.setLogStartupInfo(false);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }
}
