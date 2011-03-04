package structures;

import presage.Input;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;

public class AnswerRequestPlan extends Plan {

	public AnswerRequestPlan(APlayerDataModel dm, Interpreter interpreter,
			String myKey, String type) {
		super(dm, interpreter, myKey, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canHandle(Input input) {
		// checks if input is of the type TokenRequestMessage
		return input instanceof TokenRequestMessage;
	}

	@Override
	public void handle(Input input) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inhibits(Plan ihandler) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRemove() {
		// TODO Auto-generated method stub
		return false;
	}

}
