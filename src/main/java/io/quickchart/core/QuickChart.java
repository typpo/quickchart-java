package io.quickchart.core;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

public class QuickChart {
	private Integer width = 500;
	private Integer height = 300;
	private Double devicePixelRatio = 1.0;
	private String backgroundColor = "transparent";
	private String key;
	private String config;

	private String protocol;
	private String host;
	private Integer port;

	public QuickChart() {
		this("https", "quickchart.io", 443);
	}

	public QuickChart(String protocol, String host, Integer port) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getDevicePixelRatio() {
		return devicePixelRatio;
	}

	public void setDevicePixelRatio(double devicePixelRatio) {
		this.devicePixelRatio = devicePixelRatio;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getUrl() {
		URIBuilder builder = new URIBuilder();
		builder.setScheme(this.protocol);
		builder.setHost(this.host);
		if (port != 80 && port != 443) {
			builder.setPort(this.port);
		}
		builder.setPath("/chart");
		builder.addParameter("w", this.width.toString());
		builder.addParameter("h", this.height.toString());
		builder.addParameter("devicePixelRatio", this.devicePixelRatio.toString());
		if (!this.backgroundColor.equals("transparent")) {
			builder.addParameter("bkg", this.backgroundColor);
		}
		builder.addParameter("c", this.config);
		if (this.key != null && !this.key.isBlank()) {
			builder.addParameter("key", this.key);
		}
		return builder.toString();
	}

	private String getPostJson() {
		JSONObject jsonBuilder = new JSONObject();
		jsonBuilder.put("width", this.width.toString());
		jsonBuilder.put("height", this.height.toString());
		jsonBuilder.put("devicePixelRatio", this.devicePixelRatio.toString());
		if (!this.backgroundColor.equals("transparent")) {
			jsonBuilder.put("backgroundColor", this.backgroundColor);
		}
		jsonBuilder.put("chart", this.config);
		if (this.key != null && !this.key.isBlank()) {
			jsonBuilder.put("key", this.key);
		}
		return jsonBuilder.toString();
	}

	private HttpEntity executePost(String path) throws IOException {
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(this.protocol);
		uriBuilder.setHost(this.host);
		if (port != 80 && port != 443) {
			uriBuilder.setPort(this.port);
		}
		uriBuilder.setPath(path);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uriBuilder.toString());

		StringEntity entity = new StringEntity(this.getPostJson());
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Received invalid status code from API endpoint");
		}
		HttpEntity ret = response.getEntity();
		return ret;
	}

	public String getShortUrl() throws ClientProtocolException, IOException {
		HttpEntity entity = executePost("/chart/create");
		String rawResponse = EntityUtils.toString(entity);

		JSONTokener tokener = new JSONTokener(rawResponse);
		JSONObject jsonResponse = new JSONObject(tokener);
		return jsonResponse.getString("url");
	}

	public byte[] toByteArray() throws IOException {
		HttpEntity entity = executePost("/chart");
		return EntityUtils.toByteArray(entity);
	}
	
	public String toDataUrl() throws IOException {
		HttpEntity entity = executePost("/chart");
		return "data:image/png;base64," + Base64.getEncoder().encode(EntityUtils.toByteArray(entity));
	}

	public void toFile(String filePath) throws IOException {
		HttpEntity entity = executePost("/chart");
		BufferedHttpEntity entityBuffer = new BufferedHttpEntity(entity);

		FileOutputStream os = new FileOutputStream(new File(filePath));
		entityBuffer.writeTo(os);
	}
}
