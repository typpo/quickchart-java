package io.quickchart.examples;

import io.quickchart.core.QuickChart;

public class ToFileExample {

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
		
		chart.toFile("/tmp/java-chart.png");
	}

}
