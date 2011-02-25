package structures;

import presage.Input;

public class PositionInput implements Input {

	private long timestamp;
	private String performative = "position";
	private Location position;
	
	public PositionInput(Location position, long time) {
		this.position = position;
		this.timestamp = time;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getPositionX() {
		return position.getX();
	}

	public int getPositionY() {
		return position.getY();
	}
	
	public Location getPosition() {
		return position;
	}

	public String getPerformative() {
		return performative;
	}
	
	
}
