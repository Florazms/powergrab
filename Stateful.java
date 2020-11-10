package uk.ac.ed.inf.powergrab;

//import statements

/**
* @author Flora Zhou <s2002579@ed.ac.uk>
*/
public class Stateful extends Share {
	//class body

	public Stateful(double latitude, double longitude, long seed) {
		super(latitude, longitude, seed);
	}

	float stationCoin, stationPower, c;
	double greenDistance;
	int stationNumber;
	double f, g;
	int t = 0, l = 0;
	
	/**
	 * This is the stateful strategy for the drone.
	 *
	 */
	public void statefulStrategy() {
		drawLineHead();
		LightHouseNumber();
		int greenSize = greenHouseNum.size();  //Get the number of positive stations.
		for (step = 0; step < 250; step++) {
			double minDistance = 100;
			red();    
			//Get directions, except for those will be out of the area and connected to negative stations.
			stationCoin = 0;
			stationPower = 0;
			if (enoughPower() == true)
				break;         //If the drone has no power, it can not move.
			power -= 1.25;     //Every step costs 1.25 units power.
			if (greenSize > 0) {
				//There are positive stations which can be connected.
				for (int n = 0; n < noRed.size(); n++) {
					//Traverse every possible directions
					//Find the nearest green house in possible directions
					nextPosition = myPosition.nextPosition(noRed.get(n));
					x = nextPosition.latitude;
					y = nextPosition.longitude;   //Record the location if go to one of directions. 
					if (f == x && g == y)
						continue;   //Avoid the drone being blocked in a place.
					for (int j = 0; j < greenHouseNum.size(); j++) {
						//Record the information of the nearest positive station.
						if (map.coin[greenHouseNum.get(j)] > 0) {
							a = map.p[greenHouseNum.get(j)].latitude(); // Get each green station location
							b = map.p[greenHouseNum.get(j)].longitude();
							greenDistance = Math.sqrt((y - b) * (y - b) + (x - a) * (x - a));   
							// Calculate distances between the next position with each charging station.
							if (greenDistance < minDistance) {
								minDistance = greenDistance;
								//Record the minimum distance between the next position with each charging station.
								nextLat = x;
								nextLon = y;
								stationCoin = map.coin[greenHouseNum.get(j)];   //Record coins of the nearest positive station.
								stationPower = map.power[greenHouseNum.get(j)];    //Record power of the nearest positive station.
								stationNumber = greenHouseNum.get(j);   //Record the number of the nearest positive station.
								nextDirection = noRed.get(n);    // Record direction to the nearest station.
							}
						}
					}
				} 
				if (minDistance < 0.00025) {
					//Next step will connect to a positive charging station
					coin += stationCoin; 
					power += stationPower;        //Grab coins and power from it.
					map.coin[stationNumber] = 0;
					map.power[stationNumber] = 0;         //Set coins and power of the station to zero.
					greenSize = greenSize - 1;      //
					c = coin;
					t = 0;
				} // get coins and power from the nearest green station
				if (c == coin)
					//Judge whether coins are increase or not.
					t++;
				if (t > 25 && l < 30) {
					//The drone may be blocked by negative stations.
					random();    //Let the drone go thirty random steps to make a detour.
					l++;
				}
			} else {
				//If the drone has flied through every positive stations.
				random();    //It can go to random directions.
			}

			droneType = droneType + String.valueOf(myPosition.latitude) + "," + String.valueOf(myPosition.longitude)
					+ "," + nextDirection + "," + String.valueOf(nextLat) + "," + String.valueOf(nextLon) + ","
					+ String.valueOf(coin) + "," + String.valueOf(power) + '\n';   //Record the data for txt file.

			stepNumber();
			f = myPosition.latitude;
			g = myPosition.longitude;  //Record location before moving.
			myPosition.latitude = nextLat;
			myPosition.longitude = nextLon;   //After finishing a step, change it to start point.
		}
		drawLineBottom();
		output();
	}
}
