package structures;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Element;

import presage.EnvironmentConnector;
import presage.PlayerDataModel;
import presage.abstractparticipant.APlayerDataModel;

public class CellAgent extends AbstractAgent {

	private final Logger logger = Logger.getLogger(CellAgent.class);
	
	@Element
	protected CellPlayerModel dm;
	
	final protected Random rand = new Random();
	
	final int maxSpeed = 5;
	
	Location randomTarget = null;
	int randomSpend = 0;
	
	public CellAgent() {
		super();
	}
	
	public CellAgent(ArrayList<String> roles, String participantID, UUID authcode, Location position, int communicationRange, int simSize, ArrayList<String> tokenlist) {
		super();
		dm = new CellPlayerModel(roles, participantID, authcode, position, communicationRange, simSize, tokenlist);
	}
	
	@Override
	public void initialise(EnvironmentConnector environmentConnector) {
		super.initialise(environmentConnector);
		interpreter.addPlan(new AnswerRequestPlan(getPlayerDataModel(), interpreter, getId(), "requesttoken"));
		interpreter.addPlan(new ConnectionPlan(getPlayerDataModel(), interpreter, getId()));
		interpreter.addPlan(new MoveHandler(getPlayerDataModel(), interpreter, getId(), "position"));
		interpreter.addPlan(new TokensHandler(getPlayerDataModel(), interpreter, getId()));
	}

	@Override
	public void execute() {
		
		logger.debug("Got "+ dm.connections.size() +" connections");
		// process any inputs.
		interpreter.processInputs();

		// handle timeouts and tidy up ended plans.
		interpreter.handleTimeouts(dm.time);
		
		if(dm.getMaster() != null) {
			// follow mode
			dm.myEnvironment.act(new Follow(getId(), dm.getMaster()), getId(), dm.environmentAuthCode);
		} else {
			checkSeedNeighbourhood();
			// random movement
			if(randomTarget == null || randomSpend == 0 || dm.getLocation().equals(randomTarget)) {
				randomTarget = new Location(rand.nextInt(dm.simSize), rand.nextInt(dm.simSize));
				randomSpend = rand.nextInt(dm.simSize/maxSpeed);
			}
			Move randomMove =  Move.generateMove(getId(), dm.getLocation(), randomTarget, maxSpeed);
			dm.myEnvironment.act(randomMove, getId(), dm.environmentAuthCode);
			randomSpend--;
		}
	}
	
	private void checkSeedNeighbourhood() {
		// check if there is any cell in the neighbourhood of seed i
		// if there is cell y, compare the tokens of i and y
		// if token of i and y are identical, do nothing
		// else call attachCellToSeed(int i, int y)
		boolean connectionAttempted = false;
		for (String neighbour : dm.connections) {
			// skip slaves
			if(dm.getSlaves().contains(neighbour)) 
				continue;
			
			/*if(!connectionAttempted && rand.nextDouble() > dm.connectionProb()) {
				connectionAttempted = true;
				dm.connectTo(neighbour);
			}*/
			// 1 never talked
			// 2 time old enough
			// 3 time too new
			Integer lastRequestTime = dm.lastRequest.get(neighbour);
			if (lastRequestTime == null || dm.getTime() - lastRequestTime > 20) {
				dm.lastRequest.put(neighbour, new Integer((int) dm.getTime()));
				logger.debug(getId()+": Sending TokenRequestMessage to "+ neighbour);
				dm.myEnvironment.act(new TokenRequestMessage(neighbour, getId(), null, dm.authcodestring, dm.getTime()), getId(), dm.environmentAuthCode);
			}
		}
	
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
