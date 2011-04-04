package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import presage.abstractparticipant.APlayerDataModel;

public class CellPlayerModel extends APlayerDataModel implements HasCommunicationRange, HasConnections, HasTokens, Connectable {

	// every cell has a list of tokens
	@ElementList
	public ArrayList<String> tokenList = new ArrayList<String>();
	
	public ArrayList<String> effectiveTokens = new ArrayList<String>();
	
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
	
	@Attribute
	public int simSize;
	
	public List<String> connections = new LinkedList<String>();
	
	public String master = null;
	
	public final List<String> slaves = new ArrayList<String>();
	
	public final Map<String, Integer[]> connectionAttempts = new HashMap<String, Integer[]>();
	
	public CellPlayerModel() {
		this.myId = participantID;
	}
	
	public CellPlayerModel(ArrayList<String> roles, String participantID,
			UUID authcode, Location position, int range, int simSize, ArrayList<String> tokenlist) {
		super();
		this.myId = participantID;
		this.roles = roles;
		this.participantID = participantID;
		this.authcode = authcode;
		this.authcodestring = authcode.toString();
		this.position = position;
		this.communicationRange = range;
		this.simSize = simSize;
		this.tokenList = tokenlist;
		this.effectiveTokens = tokenList;
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
		return effectiveTokens;
	}

	@Override
	public String getMaster() {
		return this.master;
	}

	@Override
	public List<String> getSlaves() {
		return this.slaves;
	}

	@Override
	public Map<String, Integer[]> getConnectionAttempts() {
		return this.connectionAttempts;
	}

	@Override
	public void setMaster(String master) {
		this.master = master;
	}

	@Override
	public void setTokens(List<String> tokens) {
		this.effectiveTokens = new ArrayList<String>(tokens);
	}

	@Override
	public void connectTo(String target) {
		if(this.getMaster() == null) {
			Integer[] attempt = getConnectionAttempts().get(target);
			if(attempt == null || getTime() - attempt[0] > 10) {
				myEnvironment.act(new ConnectionRequestMessage(target, getId(), null, environmentAuthCode.toString(), getTime(), new Random().nextInt(), this), myId, environmentAuthCode);
			}
		}
	}

	public double connectionProb() {
		return 0.5;
	}

}
