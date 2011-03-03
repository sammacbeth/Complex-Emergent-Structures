package structures;

import java.util.ArrayList;

import presage.EnvironmentConnector;
import presage.Input;
import presage.Participant;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;

public abstract class AbstractAgent implements Participant {

	protected boolean initialised = false;

	/**
	 * Plan interpreter
	 */
	protected Interpreter interpreter;
	
	@Override
	public String getId() {
		return getInternalDataModel().getId();
	}

	@Override
	public ArrayList<String> getRoles() {
		return getInternalDataModel().getRoles();
	}
	
	public abstract APlayerDataModel getPlayerDataModel();

	/**
	 * Basic initialisation of the participant. Plans should be added to
	 * the interpreter AFTER this has been called!
	 * @see presage.Participant#initialise(presage.EnvironmentConnector)
	 */
	@Override
	public void initialise(EnvironmentConnector environmentConnector) {
		if(!initialised) {
			// initialise the data model with the environment connector
			getPlayerDataModel().initialise(environmentConnector);
			// create the interpreter object
			interpreter = new Interpreter(getPlayerDataModel().random);
			// mark as initialised
			initialised = true;
			
			// plans for all agents
			interpreter.addPlan(new NetworkConnectionsPlan(getPlayerDataModel(), interpreter, getId(), "network"));
		}
	}

	@Override
	public void onDeActivation() {
		// register with environment
		getPlayerDataModel().myEnvironment.deregister(new ENVDeRegisterRequest(getPlayerDataModel().myId, getPlayerDataModel().environmentAuthCode));
	}

	@Override
	public void setTime(long cycle) {
		getPlayerDataModel().setTime(cycle);
	}

	@Override
	public void enqueueInput(Input input) {
		// add input to interpreter
		interpreter.addInput(input);
	}

	@Override
	public void enqueueInput(ArrayList<Input> input) {
		// add inputs to interpreter
		interpreter.addInput(input);
	}

	@Override
	public void onSimulationComplete() {
		// nothing at the moment
		// TODO possible db dumps here
	}
	
	@Override
	public void onActivation() {
		StructuresRegistrationRequest request;
		ArrayList<String> roles = new ArrayList<String>();
		if(getPlayerDataModel() instanceof CellPlayerModel) {
			roles.add("cell");
			request = new StructuresRegistrationRequest(getId(), roles, (CellPlayerModel) getPlayerDataModel());
		} else if(getPlayerDataModel() instanceof SeedPlayerModel) {
			roles.add("seed");
			request = new StructuresRegistrationRequest(getId(), roles, (SeedPlayerModel) getPlayerDataModel());
		} else {
			return;
		}
		ENVRegistrationResponse response = getPlayerDataModel().myEnvironment.register(request);
		getPlayerDataModel().environmentAuthCode = response.getAuthCode();
	}

}
