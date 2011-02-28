package structures;

import java.util.ArrayList;
import java.util.UUID;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import presage.abstractparticipant.APlayerDataModel;

public class SeedPlayerModel extends APlayerDataModel {

	@Attribute
	public String participantID;
	
	@Attribute
	public String authcodestring;	
	
	public UUID authcode;
	
	@Element
	public Location position;
	
	@Attribute
	public int communicationRange;
	
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

}
