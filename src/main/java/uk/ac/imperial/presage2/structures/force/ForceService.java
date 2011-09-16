package uk.ac.imperial.presage2.structures.force;

import java.util.UUID;

import org.apache.commons.math.geometry.Vector3D;

import com.google.inject.Inject;

import uk.ac.imperial.presage2.core.environment.EnvironmentService;
import uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess;
import uk.ac.imperial.presage2.core.environment.ParticipantSharedState;
import uk.ac.imperial.presage2.core.environment.ParticipantStateTransformer;

/**
 * @author Sam Macbeth
 * 
 */
public class ForceService extends EnvironmentService {

	private static final String SS_KEY = "structures.force";

	final ForceField field;

	@Inject
	protected ForceService(EnvironmentSharedStateAccess sharedState, ForceField field) {
		super(sharedState);
		this.field = field;
	}

	public static ParticipantSharedState<HasVelocity> createSharedState(UUID id, HasVelocity vel) {
		return new ParticipantSharedState<HasVelocity>(SS_KEY, vel, id);
	}

	private HasVelocity getHasVelocity(UUID aid) {
		return (HasVelocity) this.sharedState.get(SS_KEY, aid).getValue();
	}

	public Vector3D getAgentVelocity(UUID aid) {
		return getHasVelocity(aid).getVelocity();
	}

	public void setAgentVelocity(UUID aid, final Vector3D v) {
		this.sharedState.change(SS_KEY, aid, new ParticipantStateTransformer() {
			@Override
			public void transform(ParticipantSharedState<?> state) {
				if (state.getValue() instanceof HasVelocity) {
					((HasVelocity) state.getValue()).setVelocity(v);
				}
			}
		});
	}

	public double getAgentDragCoefficient(UUID aid) {
		return getHasVelocity(aid).getDragCoefficient();
	}

	public ForceField getForceField() {
		return this.field;
	}

}
