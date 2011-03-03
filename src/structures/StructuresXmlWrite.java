package structures;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;

import presage.Environment;
import presage.EventScriptManager;
import presage.Participant;
import presage.PluginManager;
import presage.PresageConfig;
import presage.ScriptedEvent;
import presage.configure.ConfigurationWriter;

public class StructuresXmlWrite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int simSize = 500;
		int communicationRange = 20;

		PresageConfig presageConfig = new PresageConfig();
		
		presageConfig.setComment("Simulated complex emergent structures");
		presageConfig.setIterations(5000);
		presageConfig.setRandomSeed(5);
		presageConfig.setThreadDelay(50);
		presageConfig.setAutorun(false);
		
		String path = "inputfolder/structures/";
		String configpath = path +"test.xml";
		File f = new File(path);
		f.mkdir();
		
		presageConfig.setPluginsConfigPath( path +"plugins.xml");
		presageConfig.setEventscriptConfigPath(path +"methods.xml");
		presageConfig.setParticipantsConfigPath(path +"participants.xml");
		presageConfig.setEnvironmentConfigPath(path +"environment.xml");
		
		PluginManager pm = new PluginManager();
		pm.addPlugin(new StructuresViewer());
		
		EventScriptManager ms = new EventScriptManager();
		
		java.util.Random rand = new java.util.Random(presageConfig.getRandomSeed());
		
		// participants
		TreeMap<String, Participant> parts = new TreeMap<String, Participant>();
		
		// seeds
		int seedcount = 1;
		ArrayList<String> seedRoles = new ArrayList<String>();
		seedRoles.add("seed");
		TreeMap<String, SeedAgent> seeds = new TreeMap<String, SeedAgent>();

		for(int i=0; i<seedcount; i++) {
			Location l = new Location(rand.nextInt(simSize), rand.nextInt(simSize));
			seeds.put("seed"+i, new SeedAgent(seedRoles, "seed"+i, UUID.randomUUID(), l, communicationRange));
			ms.addEvent(new ScriptedEvent ( 0 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("seed"+i)));
		}
		parts.putAll(seeds);
		
		// cells
		int cellcount = 4;
		ArrayList<String> cellRoles = new ArrayList<String>();
		cellRoles.add("cell");
		TreeMap<String, CellAgent> cells = new TreeMap<String, CellAgent>();

		for(int i=0; i<cellcount; i++) {
			Location l = new Location(rand.nextInt(simSize), rand.nextInt(simSize));
			cells.put("cell"+i, new CellAgent(seedRoles, "cell"+i, UUID.randomUUID(), l, communicationRange));
			ms.addEvent(new ScriptedEvent ( 0 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("cell"+i)));
		}
		parts.putAll(cells);
		
		// environment
		StructuresEnvDataModel dmodel = new StructuresEnvDataModel("EnvDataModel", StructuresEnvDataModel.class.getCanonicalName(), 0);
		dmodel.setTime(0);
		dmodel.width = simSize;
		dmodel.height = simSize;
		Environment environment = (Environment) new StructuresEnvironment(false, 4567, dmodel);
		presageConfig.setEnvironmentClass(environment.getClass());
		
		ConfigurationWriter.write(configpath, presageConfig, parts, environment, pm, ms);
	}

}
