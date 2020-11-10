package uk.ac.ed.inf.powergrab;

//import statements

/**
* @author Flora Zhou <s2002579@ed.ac.uk>
* @version 2.3 (current version number of program)
*/

import java.io.IOException;

public class App {
	// class body
	static String date;
	static String month;
	static String day;
	// Get the information of the map.

	/**
	 * Get the location and random number.
	 * <p>
	 * Add zero before those months and days only has one number. Judge whether the
	 * input is stateful or stateless to invoke corresponding method.
	 * 
	 * @param args the information of the start point, drone type and date of the
	 *             map.
	 * @throws IOException if there is not such a map.
	 */
	public static void main(String[] args) throws IOException {
		Map map = new Map(Integer.valueOf(args[2]), Integer.valueOf(args[1]), Integer.valueOf(args[0]));
		map.downloadMap();
		month = args[1];
		day = args[0];
		if (Integer.valueOf(args[1]) < 10) {
			month = "0" + Integer.valueOf(args[1]);
		}
		if (Integer.valueOf(args[0]) < 10) {
			day = "0" + Integer.valueOf(args[0]);
		}
		if (args[6].equals("stateless")) {
			date = "stateless-" + day + "-" + month + "-" + args[2];
			Stateless drone = new Stateless(Double.valueOf(args[3]), Double.valueOf(args[4]), Integer.valueOf(args[5]));
			drone.map = map;
			drone.statelessStrategy();
		}
		if (args[6].equals("stateful")) {
			date = "stateful-" + day + "-" + month + "-" + args[2];
			Stateful drone = new Stateful(Double.valueOf(args[3]), Double.valueOf(args[4]), Integer.valueOf(args[5]));
			drone.map = map;
			drone.statefulStrategy();
		}
	}
}
