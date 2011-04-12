package structures;

import java.util.TreeMap;

import presage.Environment;
import presage.EventScriptManager;
import presage.Participant;
import presage.PluginManager;
import presage.PresageConfig;
import presage.Simulation;

public class StructuresSimulation extends Simulation {

	String scenario;
	int simID;
	int paramSet;
	
	public StructuresSimulation(String scenario, int simID, int paramSet,
			PresageConfig presageConfig, TreeMap<String, Participant> players,
			Environment environment, PluginManager pluginmanager,
			EventScriptManager esm) {
		super(presageConfig, players, environment, pluginmanager, esm);
		this.scenario = scenario;
		this.simID = simID;
		this.paramSet = paramSet;
	}

	
	
}
