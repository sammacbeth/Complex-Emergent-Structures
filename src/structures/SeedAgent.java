package structures;

import java.util.ArrayList;
import java.util.UUID;

import org.simpleframework.xml.Element;
import org.totalbeginner.tutorial.Book;

import presage.EnvironmentConnector;
import presage.PlayerDataModel;
import presage.abstractparticipant.APlayerDataModel;

public class SeedAgent extends AbstractAgent {

	@Element
	protected SeedPlayerModel dm;

	ArrayList<Token> tokens;
	
	
	public SeedAgent() {
		super();
	}
	
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
		
		// every seed will check if there is a cell within a range of 10
		
		checkSeedNeighbourhood();	
		attachCellToSeed(i,y);
		
		
	}

	private void attachCellToSeed(int i, int y) {
		// freeze position of cell y on border of seed i
		// call transferToken(i,y)
		
	}

	private void transferToken(int i, int y) {
		// copy i to y
		tokenList(y) = tokenList(i); 
	}

	
	
	private void checkSeedNeighbourhood() {
		// check if there is any cell in the neighbourhood of seed i
		// if there is cell y, compare the tokens of i and y
		// if token of i and y are identical, do nothing
		// else call attachCellToSeed(int i, int y)
		for (String neighbour : dm.connections) {
			// 1 never talked
			// 2 time old enough
			// 3 time too new
			Integer lastRequestTime = dm.lastRequest.get(neighbour);
			if (lastRequestTime == null || dm.getTime() - lastRequestTime > 20) {
				dm.lastRequest.put(neighbour, new Integer((int) dm.getTime()));
				dm.myEnvironment.act(new TokenRequestMessage(neighbour, getId(), null, dm.authcodestring, dm.getTime()), getId(), dm.authcode);
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
