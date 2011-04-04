package structures;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import presage.Plugin;
import presage.Simulation;

public class LatticePlugin extends JPanel implements Plugin {

	Simulation sim;
	
	StructuresEnvDataModel dmodel;
	
	Color seedColor = Color.RED;
	Color cellColor = Color.BLACK;
	
	int cycle = 0;
	
	public void paint(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, dmodel.width, dmodel.height);
		
		// seeds at top
		int i = 0;
		g.setColor(Color.RED);
		for(String seed : dmodel.seedModels.keySet()) {
			drawNode(g, Color.RED, 50+200*i, 50, seed);
			for(String slaves : dmodel.seedModels.get(seed).getSlaves()) {
				
			}
			i++;
		}
	}
	
	private void drawNode(Graphics g, Color c, int x, int y, String name) {
		g.setColor(c);
		g.drawOval(x, y, 50, 50);
		g.drawString(name, x+5, y+25);
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
		return "Lattice";
	}

	@Override
	public String getShortLabel() {
		return "Lattice";
	}

}
