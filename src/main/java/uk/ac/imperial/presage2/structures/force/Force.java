package uk.ac.imperial.presage2.structures.force;

import org.apache.commons.math.geometry.Vector3D;

import uk.ac.imperial.presage2.core.Action;
import uk.ac.imperial.presage2.util.location.Location;

/**
 * @author Sam Macbeth
 * 
 */
public class Force extends Vector3D implements Action {

	private static final long serialVersionUID = 1L;

	public Force(double x, double y, double z) {
		super(x, y, z);
	}

	public Force(Vector3D v) {
		this(v.getX(), v.getY(), v.getZ());
	}

	public static Force getForceTo(Location currentLocation, Vector3D currentVelocity,
			Location target, double maxThrust) {
		Vector3D path = target.subtract(currentLocation);
		return new Force(new Vector3D(maxThrust, path.normalize()));
	}

}
