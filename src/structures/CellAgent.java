package structures;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.simpleframework.xml.Element;

import presage.EnvironmentConnector;
import presage.PlayerDataModel;
import presage.abstractparticipant.APlayerDataModel;

public class CellAgent extends AbstractAgent {

	@Element
	protected CellPlayerModel dm;
	
	final protected Random rand = new Random();
	
	final int maxSpeed = 5;
	
	Location randomTarget = null;
	int randomSpend = 0;
	
	public CellAgent() {
		super();
	}
	
	public CellAgent(ArrayList<String> roles, String participantID, UUID authcode, Location position, int communicationRange, int simSize) {
		super();
		dm = new CellPlayerModel(roles, participantID, authcode, position, communicationRange, simSize);
	}
	
	@Override
	public void initialise(EnvironmentConnector environmentConnector) {
		super.initialise(environmentConnector);
	}

	@Override
	public void execute() {
		
		// process any inputs.
		interpreter.processInputs();

		// handle timeouts and tidy up ended plans.
		interpreter.handleTimeouts(dm.time);
		
		// random movement
		if(randomTarget == null || randomSpend == 0 || dm.getLocation().equals(randomTarget)) {
			randomTarget = new Location(rand.nextInt(dm.simSize), rand.nextInt(dm.simSize));
			randomSpend = rand.nextInt(dm.simSize/maxSpeed);
		}
		Move randomMove =  Move.generateMove(getId(), dm.getLocation(), randomTarget, maxSpeed);
		dm.myEnvironment.act(randomMove, getId(), dm.environmentAuthCode);
		randomSpend--;
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
