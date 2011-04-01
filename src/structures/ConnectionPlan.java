/**
 * 
 */
package structures;

import java.util.Random;

import presage.Input;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;

/**
 * @author Sam Macbeth
 *
 */
public class ConnectionPlan extends Plan {

	final Connectable c;
	
	public ConnectionPlan(APlayerDataModel dm, Interpreter interpreter,
			String myKey) {
		super(dm, interpreter, myKey, "connection");
		c = (Connectable) dm;
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.Plan#canHandle(presage.Input)
	 */
	@Override
	public boolean canHandle(Input input) {
		return input instanceof ConnectionRequestMessage || input instanceof ConnectionRequestResponse;
	}

	@Override
	public void handle(Input input) {
		
		if(input instanceof ConnectionRequestMessage) {
			// initial connection request
			final ConnectionRequestMessage request = (ConnectionRequestMessage) input;
			
			// ensure they're not already a slave or our master
			if(c.getSlaves().contains(request.getFrom()) && c.getMaster() != request.getFrom()) {
				// reject
				rejectRequest(request);
			} else if(c.getConnectionAttempts().containsKey(request.getFrom())) {
				// we sent a connection request to them, check if we should accept or reject
				final int myRand = c.getConnectionAttempts().get(request.getFrom())[1];
				final int theirRand = request.getRandom();
				if(myRand < theirRand) {
					// accept
					acceptRequest(request);
				} else if(myRand == theirRand) {
					// reject and resend
					rejectRequest(request);
					dm.myEnvironment.act(new ConnectionRequestMessage(request.getFrom(), request.getTo(), request.getFromKey(), request.getToKey(), dm.getTime(), new Random().nextInt(), c), dm.myId, dm.environmentAuthCode);
				} else {
					// reject
					rejectRequest(request);
				}
			} else {
				// accept
				acceptRequest(request);
			}
		}
		else if(input instanceof ConnectionRequestResponse) {
			final ConnectionRequestResponse response = (ConnectionRequestResponse) input;
			if(response.getPerformative().equals("accept")) {
				c.setMaster(response.getFrom());
			} else {
				if(!c.getSlaves().contains(response.getFrom())) {
					dm.myEnvironment.act(new ConnectionRequestMessage(response.getFrom(), response.getTo(), response.getFromKey(), response.getToKey(), dm.getTime(), new Random().nextInt(), c), dm.myId, dm.environmentAuthCode);
				}
			}
		}
	}
	
	private void acceptRequest(ConnectionRequestMessage request) {
		dm.myEnvironment.act(new ConnectionRequestResponse(request.getFrom(), request.getTo(), request.getFromKey(), request.getToKey(), "accept", dm.getTime()), dm.myId, dm.environmentAuthCode);
		c.getConnectionAttempts().remove(request.getFrom());
		c.getSlaves().add(request.getFrom());
	}
	
	private void rejectRequest(ConnectionRequestMessage request) {
		dm.myEnvironment.act(new ConnectionRequestResponse(request.getFrom(), request.getTo(), request.getFromKey(), request.getToKey(), "reject", dm.getTime()), dm.myId, dm.environmentAuthCode);
		c.getConnectionAttempts().remove(request.getFrom());
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.Plan#inhibits(presage.abstractparticipant.plan.Plan)
	 */
	@Override
	public boolean inhibits(Plan ihandler) {
		return false;
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.Plan#canRemove()
	 */
	@Override
	public boolean canRemove() {
		return false;
	}

}
