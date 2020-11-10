package uk.ac.ed.inf.powergrab;
//import statements

/**
* @author Flora Zhou <s2002579@ed.ac.uk>
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Feature;

public class Map {
	//Class body
	String inputYear;
	String inputMonth;
	String inputDay;  
	public float coin[] = new float[50];
	public float power[] = new float[50];
	public Point p[] = new Point[50];
	public String mapSource;
	public List<Feature> featureList = null;

	/**
	 * Get the date for the map. 
	 * <p>
	 * Add zero before those months and days only has one number.
	 * 
	 * @param year  the year of the map
	 * @param month the month of the map
	 * @param day the day of the map
	 */
	public Map(int year, int month, int day) {
		inputYear = String.valueOf(year);
		inputMonth = String.valueOf(month);
		inputDay = String.valueOf(day);
		if (month < 10) {
			inputMonth = "0" + month;
		}
		if (day < 10) {
			inputDay = "0" + day;
		}
	}

	/**
	 * Download map from the Informatics web server.
	 * <p>
	 * Get the information from a map.
	 * 
	 * @throws  IOException if there is not such a map.
	 * @throws  IOException if fail to set a connection between the web server.
	 * @throws  IOException if fail to change connection into stream.
	 */
	public void downloadMap() throws IOException {
		String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/" + inputYear + "/" + inputMonth + "/"
				+ inputDay + "/powergrabmap.geojson";
		URL mapUrl = null;
		InputStream inputStream = null;
		StringBuilder stringBuilder = new StringBuilder();
		Charset charset = StandardCharsets.UTF_8;

		FeatureCollection fc = null;
		Feature f = null;
		Geometry g = null;

		try {
			mapUrl = new URL(mapString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) mapUrl.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		conn.setDoInput(true);
		try {
			conn.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inputStream = conn.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
			while ((mapSource = bufferedReader.readLine()) != null) {
				stringBuilder.append(mapSource);
			}
		}
		mapSource = stringBuilder.toString();
		fc = FeatureCollection.fromJson(mapSource);
		featureList = fc.features();

		for (int i = 0; i < 50; i++) {
			f = featureList.get(i);
			g = f.geometry();
			coin[i] = f.getProperty("coins").getAsFloat();
			power[i] = f.getProperty("power").getAsFloat();
			p[i] = (Point) g;
		}
	}

}
