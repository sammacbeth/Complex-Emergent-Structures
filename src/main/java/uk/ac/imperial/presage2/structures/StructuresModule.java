package uk.ac.imperial.presage2.structures;

import java.util.HashSet;
import java.util.Set;

import uk.ac.imperial.presage2.core.environment.ActionHandler;
import uk.ac.imperial.presage2.core.environment.EnvironmentService;
import uk.ac.imperial.presage2.core.network.NetworkConstraint;
import uk.ac.imperial.presage2.structures.force.Force;
import uk.ac.imperial.presage2.structures.force.ForceField;
import uk.ac.imperial.presage2.structures.force.ForceHandler;
import uk.ac.imperial.presage2.structures.force.ForceService;
import uk.ac.imperial.presage2.structures.force.StaticForceField;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironmentModule;
import uk.ac.imperial.presage2.util.location.LocationService;
import uk.ac.imperial.presage2.util.location.area.AreaService;
import uk.ac.imperial.presage2.util.location.area.HasArea;
import uk.ac.imperial.presage2.util.network.NetworkModule;
import uk.ac.imperial.presage2.util.network.NetworkRangeConstraint;

import com.google.inject.AbstractModule;

/**
 * @author Sam Macbeth
 * 
 */
class StructuresModule extends AbstractModule {

	@Override
	protected void configure() {
		// environment services
		Set<Class<? extends EnvironmentService>> environmentServices = new HashSet<Class<? extends EnvironmentService>>();
		environmentServices.add(LocationService.class);
		environmentServices.add(AreaService.class);
		environmentServices.add(ForceService.class);

		// action handlers
		Set<Class<? extends ActionHandler>> actionHandlers = new HashSet<Class<? extends ActionHandler>>();
		actionHandlers.add(ForceHandler.class);

		install(new AbstractEnvironmentModule(StructuresEnvironment.class, environmentServices,
				actionHandlers));
		bind(HasArea.class).to(StructuresEnvironment.class);
		bind(ForceField.class).toInstance(new StaticForceField(new Force(0, 0, 0)));

		// install constrained network
		Set<Class<? extends NetworkConstraint>> constaints = new HashSet<Class<? extends NetworkConstraint>>();
		constaints.add(NetworkRangeConstraint.class);
		install(NetworkModule.constrainedNetworkModule(constaints).withNodeDiscovery());

	}

}
