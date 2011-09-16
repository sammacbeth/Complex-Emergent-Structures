package uk.ac.imperial.presage2.structures.force;

import uk.ac.imperial.presage2.util.location.Location;

/**
 * @author Sam Macbeth
 * 
 */
public interface ForceField {

	Force getFieldForceAt(Location l);

}
