package structures;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import presage.Plugin;
import presage.Simulation;

public class StructuresViewer extends JPanel implements Plugin {

	Simulation sim;
	
	StructuresEnvDataModel dmodel;
	
	Color seedColor = Color.RED;
	Color cellColor = Color.BLACK;
	
	int cycle = 0;
	
	int agentSize = 10;
	
	public void paint(Graphics g) {
		class Tuple<T> {
			T param1;
			T param2;
			
			public Tuple(T param1, T param2) {
				this.param1 = param1;
				this.param2 = param2;
			}
			
			public boolean equals(Tuple<T> t) {
				return (param1.equals(t.param1) && param2.equals(t.param2)) || (param1.equals(t.param2) && param2.equals(t.param1));
			}
		}
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, dmodel.width, dmodel.height);
		
		g.setColor(Color.blue);
		g.drawString("Cycle = " + dmodel.time, 5, 15);
		
		// seeds
		for(String playerID : dmodel.seedModels.keySet()) {
			SeedPlayerModel player = dmodel.seedModels.get((String) playerID);
			drawAgent(g, player.getLocation(), seedColor, "", player.getTokens());
		}
		// cells
		for(String playerID : dmodel.cellModels.keySet()) {
			CellPlayerModel player = dmodel.cellModels.get((String) playerID);
			drawAgent(g, player.getLocation(), cellColor, player.getId(), player.getTokens());
		}
		
		g.setColor(Color.BLACK);
		
		Set<Tuple<Location>> connections = new HashSet<Tuple<Location>>();
		CellPlayerModel player;
		for(String playerID : dmodel.cellModels.keySet()) {
			player = dmodel.cellModels.get(playerID);
			if(player.getMaster() != null) {
				connections.add(new Tuple<Location>(player.getLocation(), getLocation(player.getMaster())));
			}
			for(String connected : player.getSlaves()) {
				connections.add(new Tuple<Location>(player.getLocation(), getLocation(connected)));
			}
		}
		for(Tuple<Location> link : connections) {
			try {
				Location loc1 = link.param1;
				Location loc2 = link.param2;
				g.drawLine(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
			} catch (NullPointerException e) {}
		}
	}
	
	private Location getLocation(String agent) {
		try {
			return dmodel.cellModels.get(agent).getLocation();
		} catch(NullPointerException e) {
			return dmodel.seedModels.get(agent).getLocation();
		}
	}
	
	private void drawAgent(Graphics g, Location l, Color c, String name, List<String> tokens) {
		g.setColor(c);
		g.fillOval(l.getX() - agentSize/2, l.getY() - agentSize/2, agentSize, agentSize);
		//g.drawString(name, (l.getX() - 1), (l.getY() - 1));
		//g.drawString(name +":"+tokens.toString(), (l.getX() + 4), (l.getY() - 6));
	}
	
	@Override
	public void execute() {
		dmodel = (StructuresEnvDataModel) sim.getEnvDataModel();
		repaint();
		cycle++;
	}

	@Override
	public void initialise(Simulation sim) {
		this.sim = sim; 
		
		setBackground(Color.GRAY);
		
		dmodel = (StructuresEnvDataModel)sim.getEnvDataModel();
		
		repaint();
	}

	@Override
	public void onDelete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSimulationComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getLabel() {
		return "StructuresViewer";
	}

	@Override
	public String getShortLabel() {
		// TODO Auto-generated method stub
		return "StructuresViewer";
	}

}
