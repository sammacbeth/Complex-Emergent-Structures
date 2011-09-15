/**
 * 	Copyright (C) 2011 Sam Macbeth <sm1106 [at] imperial [dot] ac [dot] uk>
 */
package uk.ac.imperial.presage2.structures;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.ac.imperial.presage2.core.environment.EnvironmentRegistrationRequest;
import uk.ac.imperial.presage2.core.environment.EnvironmentService;
import uk.ac.imperial.presage2.core.environment.SharedStateAccessException;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironment;
import uk.ac.imperial.presage2.util.location.ParticipantLocationService;
import uk.ac.imperial.presage2.util.location.area.Area;
import uk.ac.imperial.presage2.util.location.area.AreaService;
import uk.ac.imperial.presage2.util.location.area.HasArea;

/**
 * @author Sam Macbeth
 * 
 */
public class StructuresEnvironment extends AbstractEnvironment implements HasArea {

	final private Logger logger = Logger.getLogger(StructuresEnvironment.class);

	final private Area simArea;

	@Inject
	public StructuresEnvironment(Area simArea) {
		super();
		this.simArea = simArea;
	}

	@Override
	protected Set<EnvironmentService> generateServices(EnvironmentRegistrationRequest request) {
		final Set<EnvironmentService> services = new HashSet<EnvironmentService>();
		try {
			services.add(new ParticipantLocationService(request.getParticipant(), this, this));
			services.add(this.getEnvironmentService(AreaService.class));
		} catch (SharedStateAccessException e) {
			logger.warn("Unable to add ParticipantLocationService to services for participant "
					+ request.getParticipantID() + ", error accessing shared state.", e);
		} catch (UnavailableServiceException e) {
			logger.warn(
					"Unable to add AreaService to services for participant"
							+ request.getParticipantID(), e);
		}
		return services;
	}

	@Override
	public Area getArea() {
		return simArea;
	}

}
