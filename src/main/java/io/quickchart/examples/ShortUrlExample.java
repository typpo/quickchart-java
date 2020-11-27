package io.quickchart.examples;

import io.quickchart.core.QuickChart;

public class ShortUrlExample {

	public static void main(String[] args) throws Exception {
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
		
		System.out.println(chart.getShortUrl());
		// Output: https://quickchart.io/chart/render/zf-8d310457-894c-46ec-bc4e-92ee8109fc5f
	}

}
