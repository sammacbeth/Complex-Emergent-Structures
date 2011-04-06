package structures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import presage.Plugin;
import presage.Simulation;
import structures.tree.Node;
import structures.tree.Tree;

public class DBPlugin implements Plugin {

	Simulation sim;
	
	StructuresEnvDataModel dmodel;
	
	Connection conn = null;
	
	String JDBCDriver;
	String connectionUrl;
	String username;
	String password;
	
	String scenario;
	int paramSet;
	
	int simID;
	
	int cycle = 0;
	
	public DBPlugin(String scenario, int paramSet, String jDBCDriver, String connectionUrl, String username,
			String password) {
		super();
		this.scenario = scenario;
		this.paramSet = paramSet;
		JDBCDriver = jDBCDriver;
		this.connectionUrl = connectionUrl;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public void execute() {
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
		
		// check for bridges
		Set<Tuple<String>> bridges = new HashSet<Tuple<String>>();
		for(Tree t : dmodel.trees) {
			for(String seed : dmodel.seedModels.keySet()) {
				if(t.treeHead.getName().equals(seed))
					continue;
				else if(t.nodes.containsKey(seed)) {
					Tuple<String> tup = new Tuple<String>(t.treeHead.getName(), seed);
					if(!bridges.contains(tup)) {
						bridges.add(tup);
						try {
							Statement st = conn.createStatement();
							st.execute("INSERT INTO bridges (simID, cycle, between, length) VALUES ("+simID+", "+cycle+", '"+tup.param1+"-"+tup.param2+"', "+t.nodes.get(seed).getLevel()+")");
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		cycle++;
	}

	@Override
	public void initialise(Simulation sim) {
		this.sim = sim; 

		dmodel = (StructuresEnvDataModel)sim.getEnvDataModel();
		
		// init jdbc
		try {
			Class.forName(JDBCDriver);
			
			conn = DriverManager.getConnection(connectionUrl, username, password);
			
			// dump sim details
			Statement st = conn.createStatement();
			st.execute("INSERT INTO simulations (id, scenario, paramSet) VALUES (DEFAULT, '"+this.scenario+"', "+this.paramSet+")");
			ResultSet r = st.getGeneratedKeys();
			if(r != null && r.next()) {
				this.simID = r.getInt(1);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShortLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
