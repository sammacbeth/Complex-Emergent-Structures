package structures;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

/**
 * Generic location coordinates within our simulation.
 * @author Sam Macbeth
 */
public class Location implements Serializable {

	@Attribute
	private int x;
	@Attribute
	private int y;
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
	
	public Location() {}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Location(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Location(Location location) {
		super();
		this.x = location.x;
		this.y = location.y;
	}

	public static Location fromPolar(int mag, double angle) {
		return new Location((int) Math.round(mag * Math.cos(angle)), (int) Math.round(mag * Math.sin(angle)));
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Gets the magnitude of these coordinates from the origin.
	 * @return
	 */
	public int getMagnitude() {
		return (int) Math.round(Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2)));
	}
	
	/**
	 * Gets the angle of this vector from the x axis.
	 * @return
	 */
	public double getAngle() {
		return Math.atan2(getY(),getX());
	}
	
	/**
	 * Returns the straight line distance between a and b
	 * @param a
	 * @param b
	 * @return
	 */
	public static int distanceBetween(Location a, Location b) {
		return Location.minus(a, b).getMagnitude();
	}
	
	/**
	 * Returns the cartesian sum of the two coordinates.
	 * @param a
	 * @param b
	 * @return
	 */
	public static Location add(Location a, Location b) {
		return new Location(a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	/**
	 * Returns cartesian sum of a and -b.
	 * @param a
	 * @param b
	 * @return
	 */
	public static Location minus(Location a, Location b) {
		return new Location(a.getX() - b.getX(), a.getY() - b.getY());
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(this != null && obj != null) {
			if(obj instanceof Location) {
				Location b = (Location) obj;
				return (this.x == b.x) && (this.y == b.y);
			}
		}
		return false;
	}
	
	
	
}
