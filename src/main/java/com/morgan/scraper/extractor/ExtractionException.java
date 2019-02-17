package com.morgan.scraper.extractor;

public class ExtractionException extends RuntimeException {
  public ExtractionException(String message, Throwable cause) {
    super(message, cause);
  }

  ExtractionException(String message) {
    super(message);
  }
}
