package structures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
		
		int simSize = 400;
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
		pm.addPlugin(new LatticePlugin());
		
		EventScriptManager ms = new EventScriptManager();
		
		java.util.Random rand = new java.util.Random(presageConfig.getRandomSeed());
		
		// participants
		TreeMap<String, Participant> parts = new TreeMap<String, Participant>();
		
		// seeds
		int seedcount = 2;
		ArrayList<String> seedRoles = new ArrayList<String>();
		seedRoles.add("seed");
		TreeMap<String, SeedAgent> seeds = new TreeMap<String, SeedAgent>();
		
		Random r = new Random();
		double achance = 2;
		double bchance = 0;
		double cchance = 0;
		ArrayList<String>[] seedTokens = new ArrayList[seedcount];
		for(int ind=0; ind<seedcount; ind++) {
			seedTokens[ind] = new ArrayList<String>();
			if(r.nextDouble() < achance) {
				seedTokens[ind].add("a");
			} else {
				achance = achance + (1-achance)/(seedcount-1);
			}
			if(r.nextDouble() < bchance) {
				seedTokens[ind].add("b");	
			} else {
				bchance = bchance + (1-bchance)/(seedcount-1);
			}
			if(r.nextDouble() < cchance) {
				seedTokens[ind].add("c");
			} else {
				cchance = cchance + (1-cchance)/(seedcount-1);
			}
		}
		
		for(int i=0; i<seedcount; i++) {
			Location l = new Location(rand.nextInt(simSize), rand.nextInt(simSize));
			seeds.put("seed"+i, new SeedAgent(seedRoles, "seed"+i, UUID.randomUUID(), l, communicationRange, seedTokens[i]));
			ms.addEvent(new ScriptedEvent ( 0 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("seed"+i)));
		}
		parts.putAll(seeds);
		
		// cells
		int cellcount = 20;
		ArrayList<String> cellRoles = new ArrayList<String>();
		cellRoles.add("cell");
		TreeMap<String, CellAgent> cells = new TreeMap<String, CellAgent>();

		achance = 0.7;
		bchance = 0.2;
		cchance = 0.5;
		ArrayList<String>[] cellTokens = new ArrayList[cellcount];
		for(int ind=0; ind<cellcount; ind++) {
			cellTokens[ind] = new ArrayList<String>();
		}
		
		for(int i=0; i<cellcount; i++) {
			Location l = new Location(rand.nextInt(simSize), rand.nextInt(simSize));
			cells.put("cell"+i, new CellAgent(seedRoles, "cell"+i, UUID.randomUUID(), l, communicationRange, simSize, cellTokens[i]));
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
