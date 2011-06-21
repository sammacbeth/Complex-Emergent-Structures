/**
 * 	Copyright (C) 2011 Sam Macbeth <sm1106 [at] imperial [dot] ac [dot] uk>
 */
package uk.ac.imperial.presage2.structures;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.imperial.presage2.core.environment.AbstractEnvironment;
import uk.ac.imperial.presage2.core.environment.ActionHandler;
import uk.ac.imperial.presage2.core.environment.EnvironmentRegistrationRequest;
import uk.ac.imperial.presage2.core.environment.EnvironmentService;
import uk.ac.imperial.presage2.core.environment.ParticipantSharedState;
import uk.ac.imperial.presage2.core.environment.SharedStateAccessException;
import uk.ac.imperial.presage2.util.environment.location.Location;
import uk.ac.imperial.presage2.util.environment.location.ParticipantLocationService;

/**
 * @author Sam Macbeth
 *
 */
public class StructuresEnvironment extends AbstractEnvironment {

	final private Logger logger = Logger.getLogger(StructuresEnvironment.class);
	
	@Override
	protected Set<ActionHandler> initialiseActionHandlers() {
		throw new NotImplementedException();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Set<EnvironmentService> generateServices(EnvironmentRegistrationRequest request) {
		final Set<EnvironmentService> services = new HashSet<EnvironmentService>();
		try {
			/*
			 * TODO this should not be done by the environment programmer. The EnvironmentService programmer should allow creation of the
			 * object through a static constructor or similar.
			 */
			services.add(new ParticipantLocationService(request.getParticipant(), (ParticipantSharedState<Location>) this.get("util.location", request.getParticipantID()), this, this));
			if(logger.isDebugEnabled()) {
				logger.debug("Added ParticipantLocationService to services for Participant "+ request.getParticipantID());
			}
		} catch(SharedStateAccessException e) {
			logger.warn("Unable to add ParticipantLocationService to services for participant "+ request.getParticipantID() +", error accessing shared state.", e);
		}
		return services;
	}

}
