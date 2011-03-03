package structures;

import java.awt.Color;
import java.awt.Graphics;

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
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, dmodel.width, dmodel.height);
		
		g.setColor(Color.blue);
		g.drawString("Cycle = " + dmodel.time, 5, 15);
		
		// seeds
		for(String playerID : dmodel.seedModels.keySet()) {
			SeedPlayerModel player = dmodel.seedModels.get((String) playerID);
			drawAgent(g, player.getLocation(), seedColor);
		}
		// cells
		for(String playerID : dmodel.cellModels.keySet()) {
			CellPlayerModel player = dmodel.cellModels.get((String) playerID);
			drawAgent(g, player.getLocation(), cellColor);
		}
	}
	
	private void drawAgent(Graphics g, Location l, Color c) {
		g.setColor(c);
		g.fillOval(l.getX() - agentSize/2, l.getY() - agentSize/2, agentSize, agentSize);
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
