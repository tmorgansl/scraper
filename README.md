# `scraper`
[![Build Status](https://travis-ci.com/tmorgansl/scraper.svg?branch=master)](https://travis-ci.com/tmorgansl/scraper)
[![License](https://img.shields.io/github/license/tmorgansl/scraper.svg)]()

A CLI application designed to scrape a dummy website for product information

## Prerequisites

* A java environment with at least version 8. Tests are run against java 8 and 11, using both openJDK and oracleJDK

## Running the application

The easiest way to run the application is to run the application using the maven wrapper and the exec plugin:-

```
./mvnw exec:java -q
```

Alternatively maven can be used to build the application and then you execute the jar file directly

```
./mvnw install
cd target
jar -jar scraper-0.0.1-SNAPSHOT.jar
```

Note for a windows environment use the `mvnw.cmd` script instead of the `mvnw` shell script. This has not been tested.

## Tests

A basic set of unit tests can be ran using the maven wrapper:-

```
./mvnw test
```

## Dependencies

### Runtime

 * gson 2.8.2
 * jsoup 1.11.3

### Plugins

 * spotbugs-maven-plugin 3.1.10
 * fmt-maven-plugin 2.4.0
 * exec-maven-plugin 1.6.0
 
## Known issues and future work

* The extractor logic has been designed to be flexible with changes to positioning of DOM elements within the page (e.g. if a whole table is moved). However, it is brittle to changes to the internal structure of the DOM elements it is looking for (e.g. if columns are moved around looking for `kcal_per_100g`). This could be resolved by being more creative with the css queries and also checking for `null` values to avoid NPEs.
* Add integration tests.
* Add a dockerfile so that users do not require a functioning java install.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details