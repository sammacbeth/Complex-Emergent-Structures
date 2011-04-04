package structures;

import java.util.ArrayList;

import presage.Input;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;

public class TokensHandler extends Plan {

	HasTokens tokens;
	Connectable conn;
	
	public TokensHandler(APlayerDataModel dm, Interpreter interpreter,
			String myKey) {
		super(dm, interpreter, myKey, "tokens");
		tokens = (HasTokens) dm;
		conn = (Connectable) dm;
	}

	@Override
	public boolean canHandle(Input input) {
		// TODO Auto-generated method stub
		return input instanceof TokensInput;
	}

	@Override
	public void handle(Input input) {
		if(input instanceof TokensInput) {
			TokensInput t = (TokensInput) input;
			tokens.setTokens(t.getTokens());
			conn.setSlaves(new ArrayList<String>(t.getSlaves()));
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
