/**
 * 
 */
package structures;

import java.util.List;

import presage.Input;

/**
 * Represents a set of agents to which an agent is physically connected.
 * @author Sam Macbeth
 */
public class ConnectionsInput implements Input {

	private long timestamp;
	private String performative = "connections";
	private List<String> connections;
	
	public ConnectionsInput(long timestamp, List<String> connections) {
		super();
		this.timestamp = timestamp;
		this.connections = connections;
	}

	/**
	 * @see presage.Input#getPerformative()
	 */
	@Override
	public String getPerformative() {
		return performative;
	}

	/**
	 * @see presage.Input#getTimestamp()
	 */
	@Override
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @see presage.Input#setTimestamp(long)
	 */
	@Override
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Get the connections being provided in this Input.
	 * @return
	 */
	public List<String> getConnections() {
		return this.connections;
	}

}
