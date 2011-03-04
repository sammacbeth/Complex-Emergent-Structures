package structures;

import java.util.ArrayList;
import java.util.List;

import presage.Message;

public class TokenRequestAnswer extends Message {

	public List<String> TokenList = new ArrayList<String>();
	
	public TokenRequestAnswer(String to, String from, String toKey,
			String fromKey, String performative, String convType, long timestamp, ArrayList<String> TokenList) {
		super(to, from, toKey, fromKey, performative, convType, timestamp);
		// TODO Auto-generated constructor stub
		this.TokenList = TokenList;
		
	}

}
