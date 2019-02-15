package com.morgan.scraper.traverser;

import java.util.Objects;

public class DOMElement {
  private final String href;

  DOMElement(final String href) {
    this.href = href;
  }

  public String getHref() {
    return href;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DOMElement DOMElement = (DOMElement) o;
    return Objects.equals(href, DOMElement.href);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href);
  }
}
