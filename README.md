quickchart-java
---
[![Maven Central](http://img.shields.io/maven-central/v/io.quickchart/QuickChart.svg?style=flat)](https://mvnrepository.com/artifact/io.quickchart/QuickChart)

A Java client for the QuickChart.io chart API.

## Installation 

Add QuickChart to your project (Java 6+).

Maven:
```xml
<dependency>
    <groupId>io.quickchart</groupId>
    <artifactId>QuickChart</artifactId>
</dependency>
```

Gradle:
```gradle
dependencies {
    implementation 'io.quickchart:Quickchart:VERSION'
}
```

## Usage

This library provides a `QuickChart` namespace containing a `Chart` class.  Import and instantiate it.  Then set properties on it and specify a [Chart.js](https://chartjs.org) config:

```java
import io.quickchart.QuickChart;

// ...

QuickChart chart = new QuickChart();
chart.setWidth(500);
chart.setHeight(300);
chart.setConfig("{"
    + "    type: 'bar',"
    + "    data: {"
    + "        labels: ['Q1', 'Q2', 'Q3', 'Q4'],"
    + "        datasets: [{"
    + "            label: 'Users',"
    + "            data: [50, 60, 70, 180]"
    + "        }]"
    + "    }"
    + "}"
);
```

For more details on configuring your chart, reference the [QuickChart documentation](https://quickchart.io/documentation/).

Use `getUrl()` on your QuickChart object to get the encoded URL that renders your chart:

```java
System.out.println(qc.getUrl());
// https://quickchart.io/chart?c=%7B%22chart%22%3A+%7B%22type%22%3A+%22bar%22%2C+%22data%22%3A+%7B%22labels%22%3A+%5B%22Hello+world%22%2C+%22Test%22%5D%2C+%22datasets%22%3A+%5B%7B%22label%22%3A+%22Foo%22%2C+%22data%22%3A+%5B1%2C+2%5D%7D%5D%7D%7D%7D&w=600&h=300&bkg=%23ffffff&devicePixelRatio=2.0&f=png
```

If you have a long or complicated chart, use `getShortUrl()` to get a fixed-length URL using the quickchart.io web service (note that these URLs only persist for a short time unless you have a subscription):

```java
System.out.println(qc.getShortUrl());
// https://quickchart.io/chart/render/f-a1d3e804-dfea-442c-88b0-9801b9808401
```

The URLs will render an image of a chart:

<img src="https://quickchart.io/chart?c=%7B%22type%22%3A+%22bar%22%2C+%22data%22%3A+%7B%22labels%22%3A+%5B%22Hello+world%22%2C+%22Test%22%5D%2C+%22datasets%22%3A+%5B%7B%22label%22%3A+%22Foo%22%2C+%22data%22%3A+%5B1%2C+2%5D%7D%5D%7D%7D&w=600&h=300&bkg=%23ffffff&devicePixelRatio=2.0&f=png" width="500" />

See [io.quickchart.examples](https://github.com/typpo/quickchart-java/tree/main/src/main/java/io/quickchart/examples) to see other example usage.

---

## Customizing your chart

You can set the following properties:

### setConfig(String)
The Chart.js chart configuration.

### setWidth(Integer)
Width of the chart image in pixels.  Defaults to 500

### setHeight(Integer)
Height of the chart image  in pixels.  Defaults to 300

### setBackgroundColor(String)
The background color of the chart. Any valid HTML color works. Defaults to #ffffff (white). Also takes rgb, rgba, and hsl values.

### setDevicePixelRatio(Double)
The device pixel ratio of the chart. This will multiply the number of pixels by the value. This is usually used for retina displays. Defaults to 1.0.

### setKey(String)
API key (not required)

---

## Creating chart URLs

There are a few ways to get a URL for your chart object.

### getUrl(): String

Returns a URL that will display the chart image when loaded.

### getShortUrl(): String

Uses the quickchart.io web service to create a fixed-length chart URL that displays the chart image.  Returns a URL such as `https://quickchart.io/chart/render/f-a1d3e804-dfea-442c-88b0-9801b9808401`.

Note that short URLs expire after a few days for users of the free service.  You can [subscribe](https://quickchart.io/pricing/) to keep them around longer.

---

## Other methods

### toFile(String path)

Write your chart to file.

### toByteArray(): byte[]

Returns an array of bytes representing your image.

### toBase64Url(): String

Returns a string suitable for display as a base 64 url beginning with `data:image/png;base64,`

## More examples

Check out the [io.quickchart.examples](https://github.com/typpo/quickchart-java/tree/main/src/main/java/io/quickchart/examples) to see other usage.
