package structures;

import java.util.TreeMap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import presage.Participant;
import presage.abstractparticipant.APlayerDataModel;
import presage.environment.AEnvDataModel;

public class StructuresEnvDataModel extends AEnvDataModel {

	@Element
	public int width;
	
	@Element
	public int height;
	
	// TODO map of participants
	@ElementMap
	public TreeMap<String, APlayerDataModel> players = new TreeMap<String, APlayerDataModel>();
	
	@ElementMap
	public TreeMap<String, SeedPlayerModel> seedModels = new TreeMap<String, SeedPlayerModel>();
	
	@ElementMap
	public TreeMap<String, CellPlayerModel> cellModels = new TreeMap<String, CellPlayerModel>();
	
	public StructuresEnvDataModel() {
		super();
	}
	
	public StructuresEnvDataModel(String environmentname, String envClass, long time) {
		super(environmentname, envClass, time);
	}
	
}
