package uk.ac.ed.inf.powergrab;

/**
 * @author Flora Zhou <s2002579@ed.ac.uk>
 */
public class Position {
	// class body
	public double latitude;
	public double longitude;

	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	} // Get the location of starting point.

	/**
	 * Get the latitude and longitude when choosing one direction.
	 */
	public Position nextPosition(Direction direction) {
		switch (direction) {
		case N:
			Position n = new Position(this.latitude + 0.0003, this.longitude);
			return n;
		case NNE:
			Position nne = new Position(this.latitude + 0.0003 * Math.sin(67.5 / 180 * Math.PI),
					this.longitude + 0.0003 * Math.cos(67.5 / 180 * Math.PI));
			return nne;
		case NE:
			Position ne = new Position(this.latitude + 0.0003 * Math.sin(45d / 180d * Math.PI),
					this.longitude + 0.0003 * Math.cos(45d / 180d * Math.PI));
			return ne;
		case ENE:
			Position ene = new Position(this.latitude + 0.0003 * Math.sin(22.5 / 180 * Math.PI),
					this.longitude + 0.0003 * Math.cos(22.5 / 180 * Math.PI));
			return ene;
		case E:
			Position e = new Position(this.latitude, this.longitude + 0.0003);
			return e;
		case ESE:
			Position ese = new Position(this.latitude - 0.0003 * Math.sin(22.5 / 180 * Math.PI),
					this.longitude + 0.0003 * Math.cos(22.5 / 180 * Math.PI));
			return ese;
		case NNW:
			Position nnw = new Position(this.latitude + 0.0003 * Math.sin(67.5 / 180 * Math.PI),
					this.longitude - 0.0003 * Math.cos(67.5 / 180 * Math.PI));
			return nnw;
		case NW:
			Position nw = new Position(this.latitude + 0.0003 * Math.sin(45d / 180d * Math.PI),
					this.longitude - 0.0003 * Math.cos(45d / 180d * Math.PI));
			return nw;
		case S:
			Position s = new Position(this.latitude - 0.0003, this.longitude);
			return s;
		case SE:
			Position se = new Position(this.latitude - 0.0003 * Math.sin(45d / 180d * Math.PI),
					this.longitude + 0.0003 * Math.cos(45d / 180d * Math.PI));
			return se;
		case SSE:
			Position sse = new Position(this.latitude - 0.0003 * Math.sin(67.5 / 180 * Math.PI),
					this.longitude + 0.0003 * Math.cos(67.5 / 180 * Math.PI));
			return sse;
		case SSW:
			Position ssw = new Position(this.latitude - 0.0003 * Math.sin(67.5 / 180 * Math.PI),
					this.longitude - 0.0003 * Math.cos(67.5 / 180 * Math.PI));
			return ssw;
		case SW:
			Position sw = new Position(this.latitude - 0.0003 * Math.sin(45d / 180d * Math.PI),
					this.longitude - 0.0003 * Math.cos(45d / 180d * Math.PI));
			return sw;
		case W:
			Position w = new Position(this.latitude, this.longitude - 0.0003);
			return w;
		case WNW:
			Position wnw = new Position(this.latitude + 0.0003 * Math.sin(22.5 / 180 * Math.PI),
					this.longitude - 0.0003 * Math.cos(22.5 / 180 * Math.PI));
			return wnw;
		case WSW:
			Position wsw = new Position(this.latitude - 0.0003 * Math.sin(22.5 / 180 * Math.PI),
					this.longitude - 0.0003 * Math.cos(22.5 / 180 * Math.PI));
			return wsw;
		default:
			return null;
		}
	}

	/**
	 * Test whether the next step is in the play area or not.
	 */
	public boolean inPlayArea() {
		if (this.latitude < 55.946233 && this.latitude > 55.942617 && this.longitude < -3.184319
				&& this.longitude > -3.192473)
			return true;
		return false;
	} 

}
