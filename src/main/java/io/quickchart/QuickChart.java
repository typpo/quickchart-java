package io.quickchart;

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
import org.apache.http.client.methods.HttpPost;

/**
 * A client for the QuickChart Chart API.
 *
 */
public class QuickChart {
	private Integer width = 500;
	private Integer height = 300;
	private Double devicePixelRatio = 1.0;
	private String backgroundColor = "transparent";
	private String key;
	private String version;
	private String config;

	private String scheme;
	private String host;
	private Integer port;

	/**
	 * Create a default QuickChart object.
	 */
	public QuickChart() {
		this("https", "quickchart.io", 443);
	}

	/**
	 * Create a QuickChart object with custom host.
	 *
	 * @param scheme HTTP or HTTPS
	 * @param host   Hostname
	 * @param port   Port
	 */
	public QuickChart(String scheme, String host, Integer port) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
	}

	/**
	 * Get the width of the chart, in pixels
	 *
	 * @return Width in pixels
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of the chart, in pixels
	 *
	 * @param width Width in pixels
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the height of the chart, in pixels
	 *
	 * @return Height in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height of the chart, in pixels
	 *
	 * @param height Height in pixels
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the device pixel ratio of chart. If this value is greater than one, the
	 * true width x height dimensions of the image will be multiplied by this value.
	 *
	 * @return device to pixel ratio
	 */
	public double getDevicePixelRatio() {
		return devicePixelRatio;
	}

	/**
	 * Set the device pixel ratio of chart. If this value is greater than one, the
	 * true width x height dimensions of the image will be multiplied by this value.
	 *
	 * @param devicePixelRatio device to pixel ratio
	 */
	public void setDevicePixelRatio(double devicePixelRatio) {
		this.devicePixelRatio = devicePixelRatio;
	}

	/**
	 * Get the background color of the chart
	 *
	 * @return Background color
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set the background color of the chart. Valid colors include named colors
	 * ("red"), hex ("#abc123"), and rgb (rgb(255, 255, 255)).
	 *
	 * @param backgroundColor Color
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Get the QuickChart API key
	 *
	 * @return API key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the QuickChart API key
	 *
	 * @param key API key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get the Chart.js version number
	 *
	 * @return Chart.js version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the Chart.js version number
	 *
	 * @param version Chart.js version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Get the Chart.js config
	 *
	 * @return Chart.js config
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * Set the Chart.js config to render
	 *
	 * @param config Chart.js config. This should be a valid JSON or Javascript
	 *               string.
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	/**
	 * Generate a URL that displays a chart
	 *
	 * @return URL that will display chart when rendered
	 */
	public String getUrl() {
		URIBuilder builder = new URIBuilder();
		builder.setScheme(this.scheme);
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
		if (this.key != null && !this.key.isEmpty()) {
			builder.addParameter("key", this.key);
		}
		if (this.version != null && !this.version.isEmpty()) {
			builder.addParameter("v", this.version);
		}
		return builder.toString();
	}

	private String getPostJson(String path) {
		JSONObject jsonBuilder = new JSONObject();
		jsonBuilder.put("chart", this.config);

		if (!path.equals("/chart/create")) {
			jsonBuilder.put("width", this.width.toString());
			jsonBuilder.put("height", this.height.toString());
			jsonBuilder.put("devicePixelRatio", this.devicePixelRatio.toString());
			if (!this.backgroundColor.equals("transparent")) {
				jsonBuilder.put("backgroundColor", this.backgroundColor);
			}
			if (this.key != null && !this.key.isEmpty()) {
				jsonBuilder.put("key", this.key);
			}
			if (this.version != null && !this.version.isEmpty()) {
				jsonBuilder.put("version", this.version);
			}
		}
		return jsonBuilder.toString();
	}

	private HttpEntity executePost(String path) throws IOException {
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(this.scheme);
		uriBuilder.setHost(this.host);
		if (port != 80 && port != 443) {
			uriBuilder.setPort(this.port);
		}
		uriBuilder.setPath(path);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uriBuilder.toString());

		StringEntity entity = new StringEntity(this.getPostJson(path), "utf-8");
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-type", "application/json");

		HttpResponse response = client.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		String detailedError = "";
		if (response.containsHeader("x-quickchart-error")) {
			detailedError = "\n\n" + response.getFirstHeader("x-quickchart-error").getValue();
		}
		if (statusCode != 200) {
			throw new RuntimeException(
					"Received invalid status code " + statusCode + " from API endpoint" + detailedError);
		}
		HttpEntity ret = response.getEntity();
		return ret;
	}

	/**
	 * Generate a shortened URL that displays the chart. This URL will eventually
	 * expire.
	 *
	 * @return A shortened URL that displays the chart or null if chart creation
	 *         failed.
	 */
	public String getShortUrl() {
		try {
			HttpEntity entity = executePost("/chart/create");
			String rawResponse = EntityUtils.toString(entity);

			JSONTokener tokener = new JSONTokener(rawResponse);
			JSONObject jsonResponse = new JSONObject(tokener);
			return jsonResponse.getString("url");
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Bytes that represent a PNG chart
	 *
	 * @return Chart bytes, or null if chart could not be rendered
	 */
	public byte[] toByteArray() {
		try {
			HttpEntity entity = executePost("/chart");
			return EntityUtils.toByteArray(entity);
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * Get a base64 data URL representation of this chart. Can be embedded on the
	 * web.
	 *
	 * @return A base64 data URI, or null if chart could not be rendered.
	 */
	public String toDataUrl() {
		try {
			HttpEntity entity = executePost("/chart");
			return "data:image/png;base64," + Base64.getEncoder().encode(EntityUtils.toByteArray(entity));
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * Write this chart image to a file (PNG format)
	 *
	 * @param filePath File path
	 * @throws IOException Thrown when file could not be written successfully
	 */
	public void toFile(String filePath) throws IOException {
		HttpEntity entity = executePost("/chart");
		BufferedHttpEntity entityBuffer = new BufferedHttpEntity(entity);

		FileOutputStream os = new FileOutputStream(new File(filePath));
		entityBuffer.writeTo(os);
	}
}
