package structures.tree;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Node implements Serializable {

	@Element
	int level;
	
	@Element
	String headNode;
	
	@Element
	Node parent;
	
	@ElementList
	Set<Node> children;
	
	@Element
	String name;
	
	public Node(int level, String headNode, Node parent, String name) {
		super();
		this.level = level;
		this.headNode = headNode;
		this.parent = parent;
		this.name = name;
		this.children = new HashSet<Node>();
	}
	
	public void addChild(Node child) {
		this.children.add(child);
	}
	
	public Node createChild(String name) {
		Node n = new Node(level+1, headNode, this, name);
		return n;
	}

	public int getLevel() {
		return level;
	}

	public String getHeadNode() {
		return headNode;
	}

	public Node getParent() {
		return parent;
	}

	public Set<Node> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node)
			return this.equals((Node) obj);
		else if(obj instanceof String)
			return this.equals((String) obj);
		else
			return super.equals(obj);
	}
	
	public boolean equals(Node n) {
		return n.name.equals(this.name);
	}
	
	public boolean equals(String s) {
		return this.name.equals(s);
	}
	
}
