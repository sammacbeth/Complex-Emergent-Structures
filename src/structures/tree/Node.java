package structures.tree;

import java.util.HashSet;
import java.util.Set;

public class Node {

	int level;
	String headNode;
	Node parent;
	Set<Node> children;
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

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return "["+this.name+", "+this.level+"]";
	}
	
}
