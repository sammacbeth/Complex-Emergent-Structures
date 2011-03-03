package structures;

import java.util.ArrayList;
import java.util.UUID;

import presage.EnvironmentConnector;
import presage.PlayerDataModel;
import presage.abstractparticipant.APlayerDataModel;

public class SeedAgent extends AbstractAgent {

	protected SeedPlayerModel dm;

	/**
	 * 
	 */
	public SeedAgent(ArrayList<String> roles, String participantID, UUID authcode, Location position, int communicationRange) {
		super();
		dm = new SeedPlayerModel(roles, participantID, authcode, position, communicationRange);
	}

	@Override
	public void initialise(EnvironmentConnector environmentConnector) {
		super.initialise(environmentConnector);
		// any other initialisation you want to do.
	}

	@Override
	public void execute() {
		
		// process any inputs.
		interpreter.processInputs();

		// handle timeouts and tidy up ended plans.
		interpreter.handleTimeouts(dm.time);
	}

	@Override
	public PlayerDataModel getInternalDataModel() {
		return dm;
	}

	@Override
	public APlayerDataModel getPlayerDataModel() {
		return dm;
	}

}
