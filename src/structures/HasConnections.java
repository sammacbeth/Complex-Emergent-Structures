package structures;

import java.util.List;

public interface HasConnections {

	public void updateConnections(List<String> connections);
	
	public List<String> getConnections();
	
}
