/**
 * 
 */
package structures;

import presage.Message;

/**
 * @author Sam Macbeth
 *
 */
public class ConnectionRequestMessage extends Message {
	
	private int random;
	
	/**
	 * @param to
	 * @param from
	 * @param toKey
	 * @param fromKey
	 * @param performative
	 * @param convType
	 * @param timestamp
	 */
	public ConnectionRequestMessage(String to, String from, String toKey,
			String fromKey, long timestamp, int random, Connectable c) {
		super(to, from, toKey, fromKey, "request", "connect", timestamp);
		this.random = random;
		c.getConnectionAttempts().put(to, new Integer[] {(int) timestamp, random});
	}

	public int getRandom() {
		return random;
	}

}
