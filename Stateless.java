package uk.ac.ed.inf.powergrab;
//import statements

/**
 * @author Flora Zhou <s2002579@ed.ac.uk>
 */
public class Stateless extends Share {
	public Stateless(double latitude, double longitude, long seed) {
		super(latitude, longitude, seed);
	}

	Position noRedPos;
	float stationCoin, stationPower;
	Direction dir;
	int stationNumber;
	double minDistance;
	int n;

	/**
	 * This is the stateless strategy for the drone.
	 *
	 */
	public void statelessStrategy() {
		drawLineHead();
		LightHouseNumber();
		for (step = 0; step < 250; step++) {
			if (enoughPower() == true)
				break;       //If the drone has no power, it can not move.
			red();      //get list of directions where the drone can move to.
			power -= 1.25;        //Every step costs 1.25 units power.
			stationCoin = 0;
			stationPower = 0;
			minDistance = 1;
			for (n = 0; n < noRed.size(); n++) {
				//Traverse every possible directions
				dir = noRed.get(n);
				noRedPos = myPosition.nextPosition(dir);
				x = noRedPos.latitude;
				y = noRedPos.longitude;    //Record the location if go to one of directions. 
				for (int j = 0; j < greenHouseNum.size(); j++) {
					//Traverse every to get the information of the nearest positive station.
					a = map.p[greenHouseNum.get(j)].latitude(); 
					b = map.p[greenHouseNum.get(j)].longitude();    // Get each station location
					distance[j] = Math.sqrt((y - b) * (y - b) + (x - a) * (x - a));
					// Calculate distances between the next position with each charging station.
					if (minDistance > distance[j]) {
						minDistance = distance[j];   
						//Record the minimum distance between the next position with each charging station.
						stationCoin = map.coin[j];   //Record coins of the nearest positive station.
						stationPower = map.power[j];   //Record power of the nearest positive station.
						stationNumber = j;    //Record the number of the nearest positive station.
						nextLat = x;
						nextLon = y;
						nextDirection = dir;    //Record direction to the nearest station.
					}
				}    
			}   
			if (minDistance < 0.00025 && stationCoin > 0) {
				//next step will connect to a positive charging station
				coin += stationCoin;    
				power += stationPower;     //Grab coins and power from it.
				map.coin[stationNumber] = 0;
				map.power[stationNumber] = 0;         //Set coins and power of the station to zero.
			} else
				//If there is no positive station in next step, go a random direction.
				random();

			droneType = droneType + String.valueOf(myPosition.latitude) + "," + String.valueOf(myPosition.longitude)
					+ "," + nextDirection + "," + String.valueOf(nextLat) + "," + String.valueOf(nextLon) + ","
					+ String.valueOf(coin) + "," + String.valueOf(power) + '\n';  
			//Record the data for txt file.
			stepNumber();
			myPosition.latitude = nextLat;
			myPosition.longitude = nextLon;  //After finishing a step, change it to start point.
		}
		drawLineBottom();
		output();
	}
}
