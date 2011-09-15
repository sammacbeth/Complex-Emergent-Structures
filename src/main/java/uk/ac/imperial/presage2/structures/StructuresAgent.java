/**
 * 	Copyright (C) 2011 Sam Macbeth <sm1106 [at] imperial [dot] ac [dot] uk>
 */
package uk.ac.imperial.presage2.structures;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.math.geometry.Vector3D;

import uk.ac.imperial.presage2.core.environment.ParticipantSharedState;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.util.environment.CommunicationRangeService;
import uk.ac.imperial.presage2.util.location.HasLocation;
import uk.ac.imperial.presage2.util.location.Location;
import uk.ac.imperial.presage2.util.location.ParticipantLocationService;
import uk.ac.imperial.presage2.util.location.area.AreaService;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;
import uk.ac.imperial.presage2.util.participant.HasCommunicationRange;
import uk.ac.imperial.presage2.util.participant.HasPerceptionRange;

/**
 * 
 * @author Sam Macbeth
 * 
 */
public abstract class StructuresAgent extends AbstractParticipant implements HasLocation,
		HasPerceptionRange, HasCommunicationRange {

	// -- agent state
	Location location;
	Vector3D velocity;

	double perceptionRange;

	double communicationRange;

	double energy;

	final double ENERGY_MAX;
	// --

	protected ParticipantLocationService locationService;

	protected AreaService areaService;

	public StructuresAgent(UUID id, String name, Location loc, double perceptionRange,
			double communicationRange, double energy, double maxEnergy) {
		super(id, name);
		this.location = loc;
		this.perceptionRange = perceptionRange;
		this.communicationRange = communicationRange;
		this.velocity = Vector3D.ZERO;
		this.ENERGY_MAX = maxEnergy;
		this.energy = Math.min(energy, ENERGY_MAX);
	}

	@Override
	public void initialise() {
		super.initialise();
		try {
			this.locationService = this.getEnvironmentService(ParticipantLocationService.class);
			this.areaService = this.getEnvironmentService(AreaService.class);
		} catch (UnavailableServiceException e) {
			logger.warn(e);
			this.locationService = null;
		}
	}

	@Override
	public double getCommunicationRange() {
		return communicationRange;
	}

	@Override
	public double getPerceptionRange() {
		return perceptionRange;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location l) {
		this.location = l;
	}

	public Vector3D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3D velocity) {
		this.velocity = velocity;
	}

	@Override
	protected Set<ParticipantSharedState<?>> getSharedState() {
		Set<ParticipantSharedState<?>> ss = super.getSharedState();
		// shared state for ParticipantLocationService
		ss.add(ParticipantLocationService.createSharedState(this.getID(), this));
		// shared state for network communication range
		ss.add(CommunicationRangeService.createSharedState(getID(), this));

		return ss;
	}

}
