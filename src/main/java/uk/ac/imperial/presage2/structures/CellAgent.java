package uk.ac.imperial.presage2.structures;

import java.util.UUID;

import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.structures.force.Force;
import uk.ac.imperial.presage2.util.location.Location;

/**
 * @author Sam Macbeth
 * 
 */
public class CellAgent extends StructuresAgent {

	private Location target = null;

	/**
	 * @param id
	 * @param name
	 * @param loc
	 * @param perceptionRange
	 * @param communicationRange
	 * @param energy
	 * @param maxEnergy
	 */
	public CellAgent(UUID id, String name, Location loc, double perceptionRange,
			double communicationRange, double energy, double maxEnergy) {
		super(id, name, loc, perceptionRange, communicationRange, energy, maxEnergy);
	}

	@Override
	protected void processInput(Input in) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute() {
		super.execute();
		this.logger.info("At location: " + this.location + ", velocity: " + this.velocity
				+ ", speed: " + this.velocity.getNorm());

		if (target == null) {
			target = new Location(Random.randomInt(areaService.getSizeX()),
					Random.randomInt(areaService.getSizeY()), Random.randomInt(areaService
							.getSizeZ()));
		}

		Force f = new Force(0, 0, 0);
		if (this.location.distanceTo(target) > 10) {
			f = Force.getForceTo(location, velocity, target, THRUST_MAX);
		}

		this.logger.info("Applying thrust: " + f);
		try {
			this.environment.act(f, getID(), authkey);
		} catch (ActionHandlingException e) {
			this.logger.warn(e);
		}
	}

}
