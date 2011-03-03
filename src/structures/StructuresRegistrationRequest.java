package structures;

import java.util.ArrayList;

import presage.abstractparticipant.APlayerDataModel;
import presage.environment.messages.ENVRegisterRequest;

public class StructuresRegistrationRequest extends ENVRegisterRequest {

	APlayerDataModel dm;
	HasCommunicationRange r;
	
	public StructuresRegistrationRequest(String myId, ArrayList<String> roles, CellPlayerModel m) {
		super(myId, roles);
		dm = m;
		r = m;
	}
	
	public StructuresRegistrationRequest(String myId, ArrayList<String> roles, SeedPlayerModel m) {
		super(myId, roles);
		dm = m;
		r = m;
	}
	
}
