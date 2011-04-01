package structures;

import presage.Action;

public class Follow implements Action {

	String participantId;
	String target;
	
	public Follow(String participantId, String target) {
		super();
		this.participantId = participantId;
		this.target = target;
	}

	public String getParticipantId() {
		return participantId;
	}

	public String getTarget() {
		return target;
	}
	
}
