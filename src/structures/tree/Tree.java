package structures.tree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

public class Tree implements Serializable {

	@Element
	public Node treeHead;
	
	@ElementMap
	public Map<String, Node> nodes = new HashMap<String, Node>();
	
	@Element
	public int depth = 0;
	
}
