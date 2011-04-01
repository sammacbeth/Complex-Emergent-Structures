package structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Element;

import presage.Action;
import presage.EnvDataModel;
import presage.Input;
import presage.Message;
import presage.Simulation;
import presage.environment.AbstractEnvironment;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;

public class StructuresEnvironment extends AbstractEnvironment {

	private final Logger logger = Logger.getLogger(StructuresEnvironment.class);
	
	@Element
	StructuresEnvDataModel dmodel;
	
	ArrayList<String> alreadyMoved = new ArrayList<String>();
	
	LinkedList<Follow> follows = new LinkedList<Follow>();
	
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
		
		follows = new LinkedList<Follow>();
	}

	@Override
	protected void updatePerceptions() {
		Set<String> players = new HashSet<String>(dmodel.cellModels.keySet());
		players.addAll(dmodel.seedModels.keySet());
		for(String player : players) {
			
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
			//logger.debug("Sending "+ connected.size() +" connections to "+player);
		}
		for(String player : dmodel.cellModels.keySet()) {
			sim.players.get(player).enqueueInput(new PositionInput(dmodel.cellModels.get(player).position, dmodel.getTime()));
		}
	}

	@Override
	public void setTime(long cycle) {
		dmodel.setTime(cycle);
	}
	
	private boolean physicallyConnected(String agent1, String agent2) {
		// distance between agents less than the smallest wireless range for an agent.
		return (distanceBetween(agent1, agent2) <= Math.min(((HasCommunicationRange) dmodel.players.get(agent1)).getCommunicationRange(), ((HasCommunicationRange) dmodel.players.get(agent2)).getCommunicationRange()));
	}
	
	private int distanceBetween(String agent1, String agent2) {
		// get the player models for each agent
		HasCommunicationRange a1 = (HasCommunicationRange) dmodel.players.get(agent1);
		HasCommunicationRange a2 = (HasCommunicationRange) dmodel.players.get(agent2);
		// calculate the straight line distance between each agent's coordinates.
		return Location.distanceBetween(a1.getLocation(), a2.getLocation());
	}

	@Override
	public ENVRegistrationResponse onRegister(
			ENVRegisterRequest registrationObject) {
		StructuresRegistrationRequest sro = (StructuresRegistrationRequest)registrationObject;
		if(sro.dm instanceof CellPlayerModel) {
			this.dmodel.cellModels.put(sro.getParticipantID(), (CellPlayerModel) sro.dm);
			this.dmodel.players.put(sro.getParticipantID(), sro.dm);
			return new ENVRegistrationResponse(sro.getParticipantID(), UUID.randomUUID()) {
			};
		} else if(sro.dm instanceof SeedPlayerModel) {
			this.dmodel.seedModels.put(sro.getParticipantID(), (SeedPlayerModel) sro.dm);
			this.dmodel.players.put(sro.getParticipantID(), sro.dm);
			return new ENVRegistrationResponse(sro.getParticipantID(), UUID.randomUUID()) {
			};
		}
		return null;
	}

	@Override
	public boolean deregister(ENVDeRegisterRequest deregistrationObject) {
		if(dmodel.cellModels.get(deregistrationObject.getParticipantID()).authcode.equals(deregistrationObject.getParticipantAuthCode())) {
			dmodel.cellModels.remove(deregistrationObject.getParticipantID());
			return true;
		} else if (dmodel.seedModels.get(deregistrationObject.getParticipantID()).authcode.equals(deregistrationObject.getParticipantAuthCode())) {
			dmodel.seedModels.remove(deregistrationObject.getParticipantID());
			return true;
		}
		return false;
	}

	@Override
	protected void onInitialise(Simulation sim) {
		this.sim = sim; 

		System.out.println(this.getClass().getCanonicalName() + ": Initialising");
		
		// TODO add handlers
		this.actionhandlers.add(new MoveHandler());
		this.actionhandlers.add(new MessageHandler());
		
		alreadyMoved = new ArrayList<String>();
	}
	
	public class MoveHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Move then it can handle it....
			if (action instanceof Move)
				return true;

			return false;

		}

		public  Input handle(Action action, String actorID){

			Move moveAction = (Move)action;

			// if they have already had one move action thay can't have another.
			if ( alreadyMoved.contains(actorID))
				return null;

			Location oldPos = dmodel.cellModels.get(actorID).getLocation();

			int dx =  moveAction.getMoveX();
			int dy = moveAction.getMoveY();
	
			Location newPos = Location.add(oldPos, new Location(dx, dy));

			// check bounds and stop where necessary
			if ( newPos.getX() > dmodel.width) {
				newPos.setX(dmodel.width);
			}
			if (newPos.getY() > dmodel.height) {
				newPos.setY(dmodel.height);
			}
			if ( newPos.getX() < 0) {
				newPos.setX(0);
			}
			if (newPos.getY() < 0) {
				newPos.setY(0);
			}

			// update the world state.
			dmodel.cellModels.get(actorID).position = newPos;

			alreadyMoved.add(actorID);
			
			// do slaves
			for(String slave : dmodel.cellModels.get(actorID).getSlaves()) {
				handle(action, slave);
				sim.players.get(slave).enqueueInput(new PositionInput(newPos, dmodel.getTime()));
			}
			
			return null;
		}
	}
	
	public class MessageHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Message then it can handle it....
			if (action instanceof Message)
				return true;

			return false;
		}

		public  Input handle(Action action, String actorID){

			Message msg = (Message)action;
			
			// What we do with messages is we send them if the agents are connected 
			// and in the case of spoofed messages i.e. where actorID != msg.getFrom() 
			// we are in fact concerned with if the spoofer is connected.
			
			if(physicallyConnected(msg.getFrom(), msg.getTo())) {
			//	We may want to drop the message to simulate other network errors, we could do that here.		
				//System.out.println("Environment Delivering: " + msg.toString());
	
				if (queueactions){
					participantInputs.get(msg.getTo()).add(msg);
				} else {
					sim.getPlayer(msg.getTo()).enqueueInput(msg);
				}
			}
			// we don't send anything to the sender
			return null;

		}
	}
	
	public class FollowHandler implements ActionHandler {

		@Override
		public boolean canHandle(Action action) {
			return action instanceof Follow;
		}

		@Override
		public Input handle(Action action, String actorID) {
			Follow f = (Follow) action;
			if(actorID == f.participantId) {
				follows.add(f);
			}
			return null;
		}
		
	}

}
