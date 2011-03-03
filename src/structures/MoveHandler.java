package structures;

import presage.Input;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;

public class MoveHandler extends Plan {

	HasLocation dm;
	
	public MoveHandler(APlayerDataModel dm, Interpreter interpreter,
			String myKey, String type) {
		super(dm, interpreter, myKey, type);
		this.dm = (HasLocation) dm;
	}

	@Override
	public boolean canHandle(Input input) {
		return (input instanceof PositionInput);
	}

	@Override
	public boolean canRemove() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle(Input input) {
		PositionInput pos = (PositionInput) input;
		dm.setLocation(pos.getPosition());
	}

	@Override
	public boolean inhibits(Plan ihandler) {
		// TODO Auto-generated method stub
		return false;
	}

}
