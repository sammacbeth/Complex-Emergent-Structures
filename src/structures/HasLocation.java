package structures;

import structures.CellPlayerModel.State;

public interface HasLocation {

	public Location getLocation();
	
	public void setLocation(Location loc);
	
	public State getState();
	
	public void setState(State s);
	
}
