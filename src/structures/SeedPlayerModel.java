package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import presage.abstractparticipant.APlayerDataModel;

public class SeedPlayerModel extends APlayerDataModel implements HasCommunicationRange, HasConnections, HasTokens {

	// every seed has a list of tokens
	public ArrayList<String> tokenList = new ArrayList<String>();
	
	public Map<String, Integer> lastRequest = new HashMap<String, Integer>();
	
	@Attribute
	public String participantID;
	
	@Attribute
	public String authcodestring;	
	
	public UUID authcode;
	
	@Element
	public Location position;
	
	@Attribute
	public int communicationRange;
	
	public List<String> connections;
	
	public SeedPlayerModel() {
		this.myId = participantID;
	}
	
	public SeedPlayerModel(ArrayList<String> roles, String participantID,
			UUID authcode, Location position, int range) {
		super();
		this.myId = participantID;
		this.roles = roles;
		this.participantID = participantID;
		this.authcode = authcode;
		this.authcodestring = authcode.toString();
		this.position = position;
		this.communicationRange = range;
	}
	
	@Override
	public void onInitialise() {
		
	}

	@Override
	public int getCommunicationRange() {
		return this.communicationRange;
	}

	@Override
	public Location getLocation() {
		return position;
	}

	@Override
	public void updateConnections(List<String> connections) {
		this.connections = connections;
	}

	@Override
	public List<String> getConnections() {
		return this.connections;
	}
	
	@Override
	public void setLocation(Location loc) {
		this.position.setX(loc.getX());
		this.position.setY(loc.getY());
	}

	@Override
	public List<String> getTokens() {
		return tokenList;
	}
	
}
