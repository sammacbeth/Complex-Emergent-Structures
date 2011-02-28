package structures;

import java.util.TreeMap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import presage.Participant;
import presage.environment.AEnvDataModel;

public class StructuresEnvDataModel extends AEnvDataModel {

	@Element
	public int width;
	
	@Element
	public int height;
	
	// TODO map of participants
	@ElementMap
	public TreeMap<String, Participant> nodeModels = new TreeMap<String, Participant>();
	
	public StructuresEnvDataModel() {
		super();
	}
	
	public StructuresEnvDataModel(String environmentname, String envClass, long time) {
		super(environmentname, envClass, time);
	}
	
}
