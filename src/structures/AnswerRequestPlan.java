package structures;

import java.util.Random;

import org.apache.log4j.Logger;

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

	private final Logger logger = Logger.getLogger(AnswerRequestPlan.class);
	HasTokens tokens;
	Connectable conn;
	
	public AnswerRequestPlan(APlayerDataModel dm, Interpreter interpreter,
			String myKey, String type) {
		super(dm, interpreter, myKey, type);
		tokens = (HasTokens) dm;
		conn = (Connectable) dm;
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
			logger.debug(dm.getId() +" responding to TokenRequest from "+ ((TokenRequestMessage) input).getFrom() +", got "+ tokens.getTokens());
			dm.myEnvironment.act(new TokenRequestAnswer(((TokenRequestMessage) input).getFrom(), dm.getId(), null, dm.environmentAuthCode.toString(), dm.getTime(), tokens.getTokens()), dm.myId, dm.environmentAuthCode);
		}
		else if (input instanceof TokenRequestAnswer) {
			// compare to existing token(s)
			if(!tokens.getTokens().containsAll(((TokenRequestAnswer) input).getTokens())) {
				logger.debug(dm.getId() +": "+ ((TokenRequestAnswer) input).getFrom() +" has tokens I want!!");
				// TODO establish a connection with cell/seed
				conn.connectTo(((TokenRequestAnswer) input).getFrom());
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
