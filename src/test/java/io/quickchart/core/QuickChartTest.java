package io.quickchart.core;

import io.quickchart.QuickChart;
import junit.framework.TestCase;

public class QuickChartTest extends TestCase {
	public void testUrl() {
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
		
		String url = chart.getUrl();
		assertTrue(url.indexOf("https://quickchart.io/chart") == 0);
		assertTrue(url.indexOf("w=500&h=300") > -1);
		assertTrue(url.indexOf("50%2C+60%2C+") > -1);
		assertTrue(url.indexOf("key=") < 0);
		assertTrue(url.indexOf("bkg=") < 0);
	}
	
	public void testUrlWithBackgroundColor() {
		QuickChart chart = new QuickChart();
		chart.setBackgroundColor("pink");
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
		
		String url = chart.getUrl();
		assertTrue(url.indexOf("https://quickchart.io/chart") == 0);
		assertTrue(url.indexOf("w=500&h=300") > -1);
		assertTrue(url.indexOf("50%2C+60%2C+") > -1);
		assertTrue(url.indexOf("key=abc123") < 0);
		assertTrue(url.indexOf("bkg=pink") > -1);
	}
	
	public void testUrlWithKey() {
		QuickChart chart = new QuickChart();
		chart.setKey("abc123");
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
		
		String url = chart.getUrl();
		assertTrue(url.indexOf("https://quickchart.io/chart") == 0);
		assertTrue(url.indexOf("w=500&h=300") > -1);
		assertTrue(url.indexOf("50%2C+60%2C+") > -1);
		assertTrue(url.indexOf("key=abc123") > -1);
		assertTrue(url.indexOf("bkg=") < 0);
	}
}
