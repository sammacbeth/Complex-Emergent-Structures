/**
 * 
 */
package structures;

import presage.Input;
import presage.Message;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.ConversationSingle;
import presage.abstractparticipant.plan.ConversationSingleFSM;
import presage.abstractparticipant.plan.FSM;
import presage.abstractparticipant.plan.Plan;
import presage.abstractparticipant.plan.State;
import presage.abstractparticipant.plan.Transition;

/**
 * @author Sam Macbeth
 *
 */
public class ConnectConvFSM extends ConversationSingleFSM {

	
	
	public ConnectConvFSM(APlayerDataModel dm, Interpreter interpreter,
			String myKey, String theirId, String theirKey) {
		super(dm, interpreter, myKey, "connect", theirId, theirKey);
		
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.ConversationSingleFSM#initialiseFSM()
	 */
	@Override
	public FSM initialiseFSM() {
		// create our conversation FSM
		FSM fsm = new FSM("connect");
		
		State initial_state;
		State awaiting_response;
		
		try {
			initial_state = new State("initial_state");
			// transition for fsm initiator: requests to connect then moves to awaiting_response state.
			initial_state.addTransition(new Transition(FSM.INITIATE, "requestconnect", "awaiting_response", 5));
		} catch(Exception e) {
			System.err.println("Error in State generation 0" + e);
			initial_state = new State("failed");
		}
		
		// awaiting_response
		try {
			awaiting_response = new State("awaiting_response");
			// timeout waiting for response
			awaiting_response.addTransition(new Transition(Plan.TIME_OUT, "timeout", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
			// accepted request
			awaiting_response.addTransition(new Transition("accept", "processreply", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
			awaiting_response.addTransition(new Transition("reject", "processreply", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
		} catch (Exception e) {
			System.err.println("Error in State generation 0" + e);
			awaiting_response = new State("failed");
		}
		return fsm;
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.ConversationSingleFSM#handleAction(presage.abstractparticipant.plan.Transition, presage.Input)
	 */
	@Override
	public void handleAction(Transition trs, Input input) {
		if(trs.getAction().equalsIgnoreCase("processreply")) {
			processReply(input);
		} else if(trs.getAction().equalsIgnoreCase("timeout")) {
			processReply(null);
		}
	}

	private void processReply(Input input) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.ConversationSingle#spawn(java.lang.String, presage.Message)
	 */
	@Override
	public ConversationSingle spawn(String myKey, Message msg) {
		// TODO Auto-generated method stub
		return new ConnectConvFSM(dm, interpreter, myKey, msg.getFrom(), msg.getFromKey());
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.Conversation#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see presage.abstractparticipant.plan.Conversation#print()
	 */
	@Override
	public void print() {
		// TODO Auto-generated method stub

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
