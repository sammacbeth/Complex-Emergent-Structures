package structures;

import java.io.File;
import java.util.TreeMap;

import presage.Environment;
import presage.EventScriptManager;
import presage.Participant;
import presage.PluginManager;
import presage.PresageConfig;
import presage.Simulation;
import presage.configure.ConfigurationLoader;

public class HeadlessRun {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		if(args.length < 4) {
			throw new Exception("too few args.");
		}
		
		String pathBase = "inputfolder/structures/";
		String scenario = args[0];
		String path = pathBase + scenario +"/test.xml";
		
		int simId = Integer.parseInt(args[1]);
		
		int paramSet = Integer.parseInt(args[2]);
		
		double connectionProb = Double.parseDouble(args[3]);
		
		double disconnectionProb = Double.parseDouble(args[4]);
		
		PresageConfig presageConfig = ConfigurationLoader.loadPresageConfig(new File(path));
		
		TreeMap<String, Participant> players = ConfigurationLoader.loadParticipants(new File(presageConfig.getParticipantsConfigPath()));

		PluginManager pluginmanager = ConfigurationLoader.loadPluginManager(new File(presageConfig.getPluginsConfigPath()));

		Environment environment = ConfigurationLoader.loadEnvironment(new File(presageConfig.getEnvironmentConfigPath()), presageConfig.getEnvironmentClass());

		EventScriptManager esm = ConfigurationLoader.loadEventScriptManager(new File(presageConfig.getEventscriptConfigPath()));

		StructuresEnvDataModel edm = (StructuresEnvDataModel) environment.getDataModel();
		edm.simID = simId;
		edm.paramSetID = paramSet;
		edm.connectionProb = connectionProb;
		edm.dissconnectionProb = disconnectionProb;
		
		Simulation sim = new StructuresSimulation(scenario, simId, paramSet, presageConfig, players, environment, pluginmanager, esm);
		
		//sim.play();
		
	}

}
