package structures;

import java.util.ArrayList;
import java.util.UUID;

import presage.PlayerDataModel;
import presage.abstractparticipant.APlayerDataModel;

public class CellAgent extends AbstractAgent {

	protected CellPlayerModel dm;
	
	public CellAgent(ArrayList<String> roles, String participantID, UUID authcode, Location position, int communicationRange) {
		super();
		dm = new CellPlayerModel(roles, participantID, authcode, position, communicationRange);
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
