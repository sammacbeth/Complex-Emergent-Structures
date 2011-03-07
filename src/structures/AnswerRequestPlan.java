package structures;

import presage.Input;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;

/**
 * Processes both {@link TokenRequestMessage}s and {@link TokenRequestAnswer}s.
 * @author Sam Macbeth
 *
 */
public class AnswerRequestPlan extends Plan {

	HasTokens tokens;
	
	public AnswerRequestPlan(APlayerDataModel dm, Interpreter interpreter,
			String myKey, String type) {
		super(dm, interpreter, myKey, type);
		tokens = (HasTokens) dm;
	}

	@Override
	public boolean canHandle(Input input) {
		// checks if input is of the type TokenRequestMessage or TokenRequestAnswer
		return (input instanceof TokenRequestMessage || input instanceof TokenRequestAnswer);
	}

	@Override
	public void handle(Input input) {
		if(input instanceof TokenRequestMessage) {
			// reply with our tokens.
			dm.myEnvironment.act(new TokenRequestAnswer(((TokenRequestMessage) input).getFrom(), dm.getId(), null, dm.keyGen.getKey(), dm.getTime(), tokens.getTokens()), dm.myId, dm.environmentAuthCode);
		}
		else if (input instanceof TokenRequestAnswer) {
			// compare to existing token(s)
			if(!tokens.getTokens().containsAll(((TokenRequestAnswer) input).getTokens())) {
				// TODO establish a connection with cell/seed
			}

		}
	}

	@Override
	public boolean inhibits(Plan ihandler) {
		return false;
	}

	@Override
	public boolean canRemove() {

		return false;
	}

}
