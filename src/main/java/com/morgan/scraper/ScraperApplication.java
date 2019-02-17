package com.morgan.scraper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morgan.scraper.extractor.traverser.JsoupTraverser;
import com.morgan.scraper.extractor.traverser.Traverser;
import com.morgan.scraper.scraper.ScrapedProduct;
import com.morgan.scraper.scraper.ScraperService;
import com.morgan.scraper.transformer.Output;
import com.morgan.scraper.transformer.OutputTransformer;
import java.util.List;
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

  @Autowired private OutputTransformer outputTransformer;

  @Bean
  public Traverser getRootTraverser(@Value("${sainsburys.url}") String baseURL) {
    return new JsoupTraverser(baseURL);
  }

  @Override
  public void run(String... args) {

    final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    List<ScrapedProduct> scrapedProductList = scraperService.getScrapedProductList();
    Output output = outputTransformer.getOutput(scrapedProductList);

    System.out.print(gson.toJson(output));
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ScraperApplication.class);
    app.setLogStartupInfo(false);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }
}
