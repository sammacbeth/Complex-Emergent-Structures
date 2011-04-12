package structures;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import presage.Participant;
import presage.abstractparticipant.APlayerDataModel;
import presage.environment.AEnvDataModel;
import structures.tree.Tree;

public class StructuresEnvDataModel extends AEnvDataModel {

	public int simID;
	public int paramSetID;
	public double connectionProb = 1.0;
	public double dissconnectionProb = 0.0;

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

	@ElementList
	public List<Tree> trees = new ArrayList<Tree>();
	
	public StructuresEnvDataModel() {
		super();
	}
	
	public StructuresEnvDataModel(String environmentname, String envClass, long time) {
		super(environmentname, envClass, time);
	}
	
}
