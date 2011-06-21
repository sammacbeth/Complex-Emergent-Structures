/**
 * 	Copyright (C) 2011 Sam Macbeth <sm1106 [at] imperial [dot] ac [dot] uk>
 */
package uk.ac.imperial.presage2.structures;

import java.util.UUID;

import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.environment.EnvironmentConnector;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.network.NetworkAdaptor;
import uk.ac.imperial.presage2.core.participant.AbstractParticipant;
import uk.ac.imperial.presage2.util.environment.location.HasLocation;
import uk.ac.imperial.presage2.util.environment.location.HasPerceptionRange;
import uk.ac.imperial.presage2.util.environment.location.Location;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 *
 * @author Sam Macbeth
 *
 */
public abstract class StructuresAgent extends AbstractParticipant implements HasLocation, HasPerceptionRange {
	
	Location loc;
	
	double perceptionRange;
	
	/**
	 * @param id
	 * @param name
	 * @param environment
	 * @param network
	 * @param time
	 */
	@Inject
	protected StructuresAgent(@Assisted UUID id, @Assisted String name,
			EnvironmentConnector environment, NetworkAdaptor network, Time time) {
		super(id, name, environment, network, time);
	}

	/**
	 * @param id
	 * @param name
	 */
	public StructuresAgent(UUID id, String name, Location loc, double perceptionRange) {
		super(id, name);
		this.loc = loc;
		this.perceptionRange = perceptionRange;
	}



	@Override
	protected void processInput(Input in) {
		
	}

}
