package structures;

import presage.Message;

public class TokenRequestMessage extends Message {

	public TokenRequestMessage(String to, String from, String toKey,
			String fromKey, long timestamp) {
		super(to, from, toKey, fromKey, "request", "requestToken", timestamp);
		// TODO Auto-generated constructor stub
	}
	

}
