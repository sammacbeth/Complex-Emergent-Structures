package structures;

import java.util.ArrayList;

import org.simpleframework.xml.Element;

import presage.EnvDataModel;
import presage.Simulation;
import presage.environment.AbstractEnvironment;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;

public class StructuresEnvironment extends AbstractEnvironment {

	@Element
	StructuresEnvDataModel dmodel;
	
	ArrayList<String> alreadyMoved = new ArrayList<String>();
	
	Simulation sim;
	
	
	public StructuresEnvironment() {
		super();
	}
	
	public StructuresEnvironment(boolean queueactions, long randomseed, StructuresEnvDataModel dmodel) {
		super(queueactions, randomseed);

		this.dmodel = dmodel;
	}

	@Override
	public EnvDataModel getDataModel() {
		return dmodel;
	}
	
	@Override
	protected void updateNetwork() {
		
	}
	
	@Override
	protected void updatePhysicalWorld() {
		alreadyMoved = new ArrayList<String>();
	}

	@Override
	public void setTime(long cycle) {
		dmodel.setTime(cycle);
	}
	
	

}
