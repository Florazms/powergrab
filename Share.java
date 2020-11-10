package uk.ac.ed.inf.powergrab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
//import statements

/**
 * @author Flora Zhou <s2002579@ed.ac.uk>
 */
public class Share {
	// class body
	Position myPosition, nextPosition;
	Direction nextDirection;
	Random randomNumber;
	double a, b, nextLat, nextLon, x, y, min;
	int step;
	double[] distance = new double[50];
	String head, line, bottom, footPath;
	float coin = 0, power = 250, staCoin;
	String droneType = ""; 
	ArrayList<Direction> inAreaDirection = new ArrayList<>();
	ArrayList<Direction> noRed = new ArrayList<>();
	ArrayList<Integer> greenHouseNum = new ArrayList<>();
	Map map;
	FeatureCollection pathLine; 

	/**
	 * Get the location and random number.
	 * 
	 * @param latitude  the latitude of the drone
	 * @param longitude the longitude of the drone
	 * @param seed      the random number
	 */
	public Share(double latitude, double longitude, long seed) {
		myPosition = new Position(latitude, longitude);
		randomNumber = new Random(seed);

	}
	
	/**
	 * Test which directions are in the play area.
	 * <p>
	 * Traverse sixteen directions and record which of them are in the play area. 
	 * If sixteen directions are not in area, it means the start point is wrong.
	 * 
	 */
	public void inArea() {
		List<Direction> directionList = Arrays.asList(Direction.values());
		Collections.shuffle(directionList, randomNumber);
		inAreaDirection.clear();
		for (int i = 0; i < 16; i++) {
			nextDirection = directionList.get(i);
			Position nextPos = myPosition.nextPosition(nextDirection);
			x = nextPos.latitude;
			y = nextPos.longitude;
			// Get the location of the next position
			if (nextPos.inPlayArea() == true)
				inAreaDirection.add(nextDirection);
			else if (i == 15 && inAreaDirection.size() == 0)
				System.out.println("Please start in play area!");
		} // Test whether the next position is in the certain area
	}

	/**
	 * Record going which directions will not be connected to negative charging stations.
	 * 
	 */
	public void red() {
		inArea();
		noRed.clear();
		for (int n = 0; n < inAreaDirection.size(); n++) {
			nextPosition = myPosition.nextPosition(inAreaDirection.get(n));
			x = nextPosition.latitude;
			y = nextPosition.longitude;
			min = 100;
			staCoin = 0;
			for (int j = 0; j < 50; j++) {
				a = map.p[j].latitude(); // Get each station location
				b = map.p[j].longitude();
				distance[j] = Math.sqrt((y - b) * (y - b) + (x - a) * (x - a));
				if (distance[j] < min) {
					min = distance[j];
					staCoin = map.coin[j];
				}
			}
			if (min < 0.00025 && staCoin >= 0 || min > 0.00025)
				noRed.add(inAreaDirection.get(n));
		}
	}
	
	/**
	 * Calculate the number of positive charging stations.
	 * 
	 */
	public void LightHouseNumber() {
		for (int i = 0; i < 50; i++) {
			if (map.coin[i] > 0) {
				greenHouseNum.add(i);
			}
		}
	}

	/**
	 * Test whether the drone has power to have next step.
	 * 
	 * @return true if power is less than 1.25 at this time, otherwise false.
	 */
	public boolean enoughPower() {
		if (power < 1.25) {
			System.out.println("Game Over!");
			return true;
		}
		return false;

	}
	
	/**
	 * Draw path line for the drone.
	 * <p>
	 * This is the first part of path line.
	 */
	public void drawLineHead() {
		head = ("{\n" + "\"type\": \"Feature\",\n" + "\"geometry\": {\n" + "\"type\": \"LineString\",\n"
				+ "\"coordinates\": [");
		line = ("[" + myPosition.longitude + "," + myPosition.latitude + "],\n");
	}

	/**
	 * Draw path line for the drone.
	 * <p>
	 * This is the last part of path line.
	 */
	public void drawLineBottom() {
		bottom = ("]\n" + "},\n" + "\"properties\": {\n" + "}\n" + "}");
		footPath = head + line + bottom;
		Feature path = Feature.fromJson(footPath);
		map.featureList.add(path);
		pathLine = FeatureCollection.fromFeatures(map.featureList);
	}

	/**
	 * Go a random direction from those can not be connected to the negative charging stations.
	 * <p>
	 * If there is no positive charging station around.
	 */
	public void random() {
		nextDirection = noRed.get(0);
		nextPosition = myPosition.nextPosition(nextDirection);
		nextLat = nextPosition.latitude;
		nextLon = nextPosition.longitude;
	}

	/**
	 * Test whether it is the last step
	 * <p>
	 * If it is not the last step, a comma will be add to the coordinates.
	 * If it is the last step, there is no comma at last.
	 */
	public void stepNumber() {
		if (step != 249)
			line = line + "[" + nextLon + "," + nextLat + "],";
		else
			line = line + "[" + nextLon + "," + nextLat + "]";
	}

	/**
	 * Create txt and geojson files.
	 * <p>
	 * txt file contains the first location, next location and the value of coins and power of the drone.
	 * geojson file contains the information of charging stations
	 * and the path line which can also be shown in the map.
	 * 
	 */
	public void output() {
		try {
			FileWriter fileWriter = new FileWriter(App.date + ".txt");
			fileWriter.write(droneType);
			fileWriter.close();
		} catch (IOException e1) {
			System.out.println("Error during reading/writing");
		}
		try {
			FileWriter fileWriter = new FileWriter(App.date + ".geojson");
			fileWriter.write(pathLine.toJson());
			fileWriter.close();
		} catch (IOException e1) {
			System.out.println("Error during reading/writing");
		}

	}
}
