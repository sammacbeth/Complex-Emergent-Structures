/**
 * 
 */
package structures;

import presage.Input;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;

/**
 * @author sm1106
 *
 */
public class NetworkConnectionsPlan extends Plan {

	/**
	 * dm upgraded to DistPlayerModel so we have full access
	 * to agent's perceptions.
	 */
	protected APlayerDataModel dm;
	protected HasConnections cons;
	
	public NetworkConnectionsPlan(APlayerDataModel dm, Interpreter interpreter,
			String myKey, String type) {
		super(dm, interpreter, myKey, type);
		this.updateTimeout(Plan.NO_TIMEOUT);
		this.dm = dm;
		this.cons = (HasConnections) dm;
	}

	/**
	 * @see presage.abstractparticipant.plan.Plan#canHandle(presage.Input)
	 */
	@Override
	public boolean canHandle(Input input) {
		// can handle only connection inputs
		if(input instanceof ConnectionsInput)
			return true;
		else
			return false;
	}

	/**
	 * @see presage.abstractparticipant.plan.Plan#canRemove()
	 */
	@Override
	public boolean canRemove() {
		return false;
	}

	/**
	 * Handles an inform from the environment about physical connections the
	 * agent has. It updates it's perceptions of the network.
	 * @see presage.abstractparticipant.plan.Plan#handle(presage.Input)
	 */
	@Override
	public void handle(Input input) {
		if(input instanceof ConnectionsInput) {
			ConnectionsInput c = (ConnectionsInput) input;
			
			cons.updateConnections(c.getConnections());
			
		}
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.Plan#inhibits(presage.abstractparticipant.plan.Plan)
	 */
	@Override
	public boolean inhibits(Plan ihandler) {
		// TODO Auto-generated method stub
		return false;
	}

}
