package uk.ac.imperial.presage2.structures.force;

import org.apache.commons.math.geometry.Vector3D;

import uk.ac.imperial.presage2.util.location.HasLocation;

/**
 * @author Sam Macbeth
 * 
 */
public interface HasVelocity extends HasLocation {

	Vector3D getVelocity();

	void setVelocity(Vector3D v);

	double getDragCoefficient();

}
