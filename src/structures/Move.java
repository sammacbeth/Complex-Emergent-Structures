package structures;

import presage.Action;

public class Move implements Action {
	
	String participantId;
	int moveX;
	int moveY;
	
	public String getParticipantId() {
		return participantId;
	}

	public Move(String participantId, int moveX, int moveY) {
		// super(participantId, participantAuthCode);
		// TODO Auto-generated constructor stub
		this.participantId = participantId;
		this.moveX = moveX;
		this.moveY = moveY;
	}
	
	/**
	 * Builder method to generate a valid move given our target destination
	 * and max speed. Will create a move in a straight line towards the target.
	 * @param currentLocation
	 * @param targetLocation
	 * @param maxSpeed
	 * @return
	 */
	public static Move generateMove(String participantId, Location currentLocation, Location targetLocation, int maxSpeed) {
		// calculate distance to destination
		Location toGo = Location.minus(targetLocation, currentLocation);
		int distanceTo = toGo.getMagnitude();
		if(currentLocation.equals(targetLocation))
			return new Move(participantId, 0, 0);
		else if(distanceTo <= maxSpeed)
			return new Move(participantId, toGo.getX(), toGo.getY());
		else {
			int mag = maxSpeed;
			double angle = toGo.getAngle();
			Location move = Location.fromPolar(mag, angle);
			return new Move(participantId, move.getX(), move.getY());
		}
	}
	
	public void add(Move m) {
		this.moveX += m.moveX;
		this.moveY += m.moveY;
	}
	
	public int getMoveX(){
		return this.moveX;
	}
	
	public int getMoveY(){
		return this.moveY;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Move: ("+ getMoveX() +", "+ getMoveX() +")";
	}
	
	
}
