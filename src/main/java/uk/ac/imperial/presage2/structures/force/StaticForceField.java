package uk.ac.imperial.presage2.structures.force;

import uk.ac.imperial.presage2.util.location.Location;

/**
 * @author Sam Macbeth
 * 
 */
public class StaticForceField implements ForceField {

	final private Force f;

	public StaticForceField(Force f) {
		super();
		this.f = f;
	}

	@Override
	public Force getFieldForceAt(Location l) {
		return f;
	}

}
