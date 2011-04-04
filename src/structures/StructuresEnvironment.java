package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	
	LinkedList<Force> forces = new LinkedList<Force>();
	
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
		
		for(Follow f : follows) {
			Location fLoc = dmodel.cellModels.get(f.getParticipantId()).getLocation(); // follow is always a cell
			Location targetLoc = ((HasCommunicationRange) dmodel.players.get(f.getTarget())).getLocation();
			// if locations are equal move f out of the way randomly
			if(fLoc.equals(targetLoc)) {
				fLoc = Location.add(fLoc, new Location(random.nextInt(10)-10, random.nextInt(10)-10));
			} else {
				double angle = Location.minus(targetLoc, fLoc).getAngle();
				Location offset = Location.fromPolar(-15, angle);
				Location t = Location.add(targetLoc, offset);
				Move m = Move.generateMove(f.getParticipantId(), fLoc, t, 2);
				if(m != null) {
					logger.debug("Calculating follow move: "+f.getParticipantId()+"->"+f.getTarget()+" ang="+ angle +" offset="+offset+" targetloc="+t+" move="+m);
					//act(m, f.getParticipantId(), dmodel.cellModels.get(f.getParticipantId()).environmentAuthCode);
					forces.add(Force.fromMove(m));
				}
			}
			//forces.add(Force.fromMove(Move.generateMove(f.getParticipantId(), fLoc, targetLoc, 2)));
		}
		
		for(Force m : forces) {
			Location l = dmodel.cellModels.get(m.getParticipantId()).getLocation();
			for(String cell : dmodel.cellModels.keySet()) {
				if(cell == m.getParticipantId()) {
					continue;
				}
				m.add(getForceOn(l, dmodel.cellModels.get(cell).getLocation()));
			}
			for(String seed : dmodel.seedModels.keySet()) {
				m.add(getForceOn(l, dmodel.seedModels.get(seed).getLocation()));
			}
			processMove(m.toMove());
		}
		
		forces = new LinkedList<Force>();
		follows = new LinkedList<Follow>();
	}
	
	/**
	 * Force item at location b exerts on item at location a
	 * @param a
	 * @param b
	 * @return
	 */
	private Force getForceOn(Location a, Location b) {
		final int maxforce = 6;
		final int repulsionDistance = 15;
		int distance = Location.distanceBetween(a, b);
		double mag;
		if(distance < 10) {
			mag = maxforce;
			//mag = -(maxforce/(repulsionDistance*repulsionDistance)) * distance*distance + maxforce;
		} else if(distance < 15) {
			mag = 2;
		} else {
		
			mag = 0;
			return new Force(null, 0,0);
		}
		double angle = Location.minus(a, b).getAngle();
		return Force.fromPolar(mag, angle);
	}
	
	private void processMove(Move moveAction) {
		Location oldPos = dmodel.cellModels.get(moveAction.getParticipantId()).getLocation();

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
		dmodel.cellModels.get(moveAction.getParticipantId()).position = newPos;
	}

	@Override
	protected void updatePerceptions() {
		
		/**
		 * Utility class for datastructure of tokens each cell has/
		 */
		class CellTokenMap {
			Map<String,Set<String>> cellTokens = new HashMap<String, Set<String>>(dmodel.cellModels.size());
			
			public void addToken(String cell, String token) {
				if(cellTokens.containsKey(cell)) {
					cellTokens.get(cell).add(token);
				} else {
					cellTokens.put(cell, new HashSet<String>());
					addToken(cell, token);
				}
			}
			
			public void addTokens(String cell, List<String> tokens) {
				if(cellTokens.containsKey(cell)) {
					cellTokens.get(cell).addAll(tokens);
				} else {
					cellTokens.put(cell, new HashSet<String>());
					addTokens(cell, tokens);
				}
			}
			
			public Map<String,Set<String>> getMap() {
				return cellTokens;
			}
			
			public Set<String> getTokens(String cell) {
				if(cellTokens.containsKey(cell)) {
					return cellTokens.get(cell);
				} else {
					cellTokens.put(cell, new HashSet<String>());
					return getTokens(cell);
				}
			}
		}
		
		// Network connections
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
		
		// token propagation + interagent connections
		CellTokenMap cellTokens = new CellTokenMap();
		CellTokenMap connections = new CellTokenMap(); // this data structure works for connections too!
		// find seed -> cell connections & propagate tokens
		for(String seed : dmodel.seedModels.keySet()) {
			SeedPlayerModel spm = dmodel.seedModels.get(seed);
			for(String cell : spm.getSlaves()) {
				cellTokens.addTokens(cell, spm.getTokens());
			}
			/*for(String cell : spm.getConnections()) {
				cellTokens.addTokens(cell, spm.getTokens());
			}*/
		}
		// iterate cells and propagate tokens to their connected
		for(String cell : dmodel.cellModels.keySet()) {
			CellPlayerModel cpm = dmodel.cellModels.get(cell);
			Set<String> myTokens = cellTokens.getTokens(cell);
			List<String> myTokensList = new ArrayList<String>(myTokens);
			// master
			if(cpm.getMaster() != null) {
				cellTokens.addTokens(cpm.getMaster(), myTokensList);
				myTokens.addAll(cellTokens.getTokens(cpm.getMaster()));
				myTokensList = new ArrayList<String>(myTokens);
				connections.addToken(cpm.getMaster(), cell);
			}
			// slaves
			for(String slave : cpm.getSlaves()) {
				cellTokens.addTokens(slave, myTokensList);
				myTokens.addAll(cellTokens.getTokens(slave));
				myTokensList = new ArrayList<String>(myTokens);
			}
			// weak connections
			/*for(String conns : cpm.getConnections()) {
				cellTokens.addTokens(conns, myTokensList);
			}*/
		}
		// notify players of their connections + position
		for(String player : dmodel.cellModels.keySet()) {
			sim.players.get(player).enqueueInput(new TokensInput(dmodel.getTime(), new ArrayList<String>(cellTokens.getTokens(player)), connections.getTokens(player)));
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
		this.actionhandlers.add(new FollowHandler());
		
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

			/*Location oldPos = dmodel.cellModels.get(actorID).getLocation();

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
			dmodel.cellModels.get(actorID).position = newPos;*/
			forces.add(Force.fromMove(moveAction));
			alreadyMoved.add(actorID);
			
			// do slaves
			/*for(String slave : dmodel.cellModels.get(actorID).getSlaves()) {
				handle(action, slave);
				//sim.players.get(slave).enqueueInput(new PositionInput(newPos, dmodel.getTime()));
			}*/
			
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
