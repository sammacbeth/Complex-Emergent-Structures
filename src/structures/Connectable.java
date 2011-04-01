/**
 * 
 */
package structures;

import java.util.List;
import java.util.Map;

/**
 * Interface for the data model of an agent who can connect
 * to other agents.
 * 
 * @author Sam Macbeth
 *
 */
public interface Connectable {

	/**
	 * Get this agent's master (who they're connected to). They may only have
	 * one master so we simply return their participant id, null if not connected.
	 * 
	 * @return participant id of master; null if not connected.
	 */
	public String getMaster();
	
	/**
	 * 
	 */
	public void setMaster(String master);
	
	/**
	 * Get the slaves of this agent (who is connected to it).
	 * @return List of participant IDs of connected agents.
	 */
	public List<String> getSlaves(); 
	
	/**
	 * Gets connection attempts we have made.
	 * @return Map of String participant ID to array of the time of the attempt and
	 * 		random value send (at indexes 0 and 1 respectively).
	 */
	public Map<String, Integer[]> getConnectionAttempts();
	
}
