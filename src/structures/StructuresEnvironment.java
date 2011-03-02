package structures;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;

import distribution.Location;
import distribution.participants.DistPlayerModel;

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
	protected void updatePerceptions() {
		for(String player : dmodel.players.keySet()) {
			
			List<String> connected = new ArrayList<String>();
			
			// cells
			for(String cell : dmodel.cellModels.keySet()) {
				if(!player.equals(cell) && physicallyConnected(player, cell)) {
					connected.add(cell);
				}
			}
			// seeds
			for(String seed : dmodel.seedModels.keySet()) {
				if(!player.equals(seed) && physicallyConnected(player, seed)) {
					connected.add(seed);
				}
			}
			sim.players.get(player).enqueueInput(new ConnectionsInput(dmodel.getTime(), connected));
			
		}
	}

	@Override
	public void setTime(long cycle) {
		dmodel.setTime(cycle);
	}
	
	private boolean physicallyConnected(String agent1, String agent2) {
		// distance between agents less than the smallest wireless range for an agent.
		return (distanceBetween(agent1, agent2) <= Math.min(((HasCommunicationRange) dmodel.players.get(agent1).getInternalDataModel()).getCommunicationRange(), ((HasCommunicationRange) dmodel.players.get(agent2).getInternalDataModel()).getCommunicationRange()));
	}
	
	private int distanceBetween(String agent1, String agent2) {
		// get the player models for each agent
		HasCommunicationRange a1 = (HasCommunicationRange) dmodel.players.get(agent1).getInternalDataModel();
		HasCommunicationRange a2 = (HasCommunicationRange) dmodel.players.get(agent2).getInternalDataModel();
		// calculate the straight line distance between each agent's coordinates.
		return Location.distanceBetween(a1.getLocation(), a2.getLocation());
	}

}
