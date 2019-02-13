package com.morgan.scraper;

import com.morgan.scraper.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScraperApplication implements CommandLineRunner {

  @Autowired private HelloWorldService helloWorldService;

  @Override
  public void run(String... args) {
    System.out.println(this.helloWorldService.getHelloMessage());
    if (args.length > 0 && args[0].equals("exitcode")) {
      throw new RuntimeException();
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(ScraperApplication.class, args);
  }
}
