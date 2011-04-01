/**
 * 
 */
package structures;

import presage.Message;

/**
 * @author Sam Macbeth
 *
 */
public class ConnectionRequestResponse extends Message {

	public ConnectionRequestResponse(String to, String from, String toKey,
			String fromKey, String performative, long timestamp) {
		super(to, from, toKey, fromKey, performative, "connection", timestamp);
	}

}
