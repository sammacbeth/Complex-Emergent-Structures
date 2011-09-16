package uk.ac.imperial.presage2.structures.force;

import java.util.UUID;

import org.apache.commons.math.geometry.Vector3D;

import com.google.inject.Inject;

import uk.ac.imperial.presage2.core.Action;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider;
import uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.util.location.Move;
import uk.ac.imperial.presage2.util.location.MoveHandler;
import uk.ac.imperial.presage2.util.location.area.HasArea;

/**
 * @author Sam Macbeth
 * 
 */
public class ForceHandler extends MoveHandler {

	final ForceService forceService;

	@Inject
	public ForceHandler(HasArea environment, EnvironmentServiceProvider serviceProvider,
			EnvironmentSharedStateAccess sharedState) throws UnavailableServiceException {
		super(environment, serviceProvider, sharedState);
		forceService = serviceProvider.getEnvironmentService(ForceService.class);
	}

	@Override
	public boolean canHandle(Action action) {
		return action instanceof Force;
	}

	@Override
	public Input handle(Action action, UUID actor) throws ActionHandlingException {
		if (action instanceof Force) {
			Force t = (Force) action;
			Vector3D currentVelocity = forceService.getAgentVelocity(actor);
			// find resultant force
			Vector3D resultant = Vector3D.ZERO;
			// add thrust force
			resultant = resultant.add(t);
			// TODO add flow force
			resultant = resultant.add(forceService.getForceField().getFieldForceAt(
					locationService.getAgentLocation(actor)));
			// TODO add repulsion of other agents
			// add drag ( currentVelocity ^ 2 * dragCoeff * -1 )
			Vector3D drag = Vector3D.ZERO;
			if (currentVelocity.getNorm() > 0) {
				drag = drag.add(new Vector3D(currentVelocity.getNormSq()
						* forceService.getAgentDragCoefficient(actor) * -1, currentVelocity
						.normalize()));
			}

			// calculate new velocity - intermediate stage
			Vector3D newVelocity = currentVelocity.add(resultant).add(drag);

			// secondary drag component
			if (newVelocity.getNorm() > 0) {
				drag = drag
						.add(new Vector3D(newVelocity.getNormSq()
								* forceService.getAgentDragCoefficient(actor) * -1, newVelocity
								.normalize()));
			}
			drag = drag.scalarMultiply(0.5);

			// final velocity using drag average
			newVelocity = currentVelocity.add(resultant).add(drag);

			forceService.setAgentVelocity(actor, newVelocity);

			// calculate new location
			super.handle(new Move(newVelocity), actor);
			return null;
		}
		throw new ActionHandlingException("Cannot handle action " + action);
	}

}
