package structures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import presage.Plugin;
import presage.Simulation;

public class StructuresViewer extends JPanel implements Plugin {

	private static final boolean doImages = false;

	private static final String imageFolder = "/home/sm1106/Pictures/1/";

	Simulation sim;
	
	StructuresEnvDataModel dmodel;
	
	Color seedColor = Color.RED;
	Color cellColor = new Color(0, 255, 0, 100);
	Color textColor = Color.BLACK;
	
	JButton tokenButton = new JButton("Toggle Tokens");
	
	int cycle = 0;
	
	boolean showTokens = false;
	
	private static final int sizeMod = 3;
	
	int agentSize = 10*sizeMod;
	
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
		g.fillRect(0, 0, dmodel.width*sizeMod, dmodel.height*sizeMod);
		
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
			for(String proxy : player.proxies) {
				if(Location.distanceBetween(player.getLocation(), getLocation(proxy)) <= 20) {
					connections.add(new Tuple<Location>(player.getLocation(), getLocation(proxy)));
				}
			}
		}
		for(Tuple<Location> link : connections) {
			try {
				Location loc1 = link.param1;
				Location loc2 = link.param2;
				g.drawLine(loc1.getX()*sizeMod, loc1.getY()*sizeMod, loc2.getX()*sizeMod, loc2.getY()*sizeMod);
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
		g.fillOval(l.getX()*sizeMod - agentSize/2, l.getY()*sizeMod - agentSize/2, agentSize, agentSize);
		//g.drawString(name, (l.getX() - 1), (l.getY() - 1));
		if(showTokens) {
			g.setColor(textColor);
			g.drawString(name+":"+tokens.toString(), (l.getX()*sizeMod + 4*sizeMod), (l.getY()*sizeMod - 4*sizeMod));
		}
	}
	
	@Override
	public void execute() {
		dmodel = (StructuresEnvDataModel) sim.getEnvDataModel();
		repaint();
		if(doImages) {
			BufferedImage image = new BufferedImage(dmodel.width, dmodel.height, BufferedImage.TYPE_INT_RGB);
			paint(image.getGraphics());
			try {
				File f = new File(imageFolder+cycle+".png");
				ImageIO.write(image, "PNG", f);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		cycle++;
	}

	@Override
	public void initialise(Simulation sim) {
		this.sim = sim; 
		
		setBackground(Color.GRAY);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.add(this.tokenButton);
		
		this.tokenButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showTokens = !showTokens;
				repaint();
			}
		});
		
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
