package structures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import presage.Plugin;
import presage.Simulation;
import structures.tree.Node;
import structures.tree.Tree;

public class LatticePlugin extends JPanel implements Plugin {

	Simulation sim;
	
	StructuresEnvDataModel dmodel;
	
	Color seedColor = Color.RED;
	Color cellColor = Color.BLACK;
	
	JButton treeButton = new JButton("Generate Tree");
	
	private static final String Folder = "/home/sm1106/Dropbox/Photos/Structures/";
	
	int cycle = 0;
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.add(this.treeButton);
		
		g.setColor(cellColor);
		if(dmodel.trees != null) {
			g.drawString(dmodel.trees.size() +" trees.", 5, 15);
			
			int i=0;
			for(Tree t : dmodel.trees) {
				g.drawString("Tree for "+t.treeHead.getName()+": "+t.nodes.size()+" nodes.", 5, 30+15*i);
				i++;
			}
		}
	}
	
	
	@Override
	public void execute() {
		dmodel = (StructuresEnvDataModel) sim.getEnvDataModel();
		repaint();
		//writeDotFile();
		cycle++;
	}

	@Override
	public void initialise(Simulation sim) {
		this.sim = sim; 
		
		setBackground(Color.GRAY);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.add(this.treeButton);
		
		this.treeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				writeDotFile();
			}

			
		});
		
		dmodel = (StructuresEnvDataModel)sim.getEnvDataModel();
		
		try {
			File f = new File(Folder+dmodel.simID);
			f.mkdir();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		repaint();
	}
	
	private void writeDotFile() {
		try {
			File f;
			if(sim instanceof StructuresSimulation) {
				f = new File(Folder+dmodel.simID+"/FinalTree.dot");
			} else {
				f = new File(Folder+cycle+".dot");
			}
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write("digraph celltrees {");
			w.newLine();
			w.write("graph [rankdir=TB];");
			w.newLine();
			for(Tree t : dmodel.trees) {
				writeTreeNode(w, t.treeHead);
				/*SortedSet<Node> s = new TreeSet<Node>(new Comparator<Node>() {

					@Override
					public int compare(Node o1, Node o2) {
						return o1.getLevel() - o2.getLevel();
					}
				});
				s.addAll(t.nodes.values());*/
				/*for(Node n : t.nodes.values()) {
					for(Node child : n.getChildren()) {
						w.write(n.getName() +" -> "+ child.getName());
						w.newLine();
					}
				}*/
				
				break;
			}
			w.write("}");
			w.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeTreeNode(BufferedWriter w, Node n) throws IOException {
		for(Node child : n.getChildren()) {
			w.write(n.getName() +" -> "+ child.getName());
			w.newLine();
			writeTreeNode(w, child);
		}
	}

	@Override
	public void onDelete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSimulationComplete() {
		writeDotFile();
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
