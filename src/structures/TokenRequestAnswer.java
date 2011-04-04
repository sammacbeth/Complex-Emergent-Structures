package structures;

import java.util.ArrayList;
import java.util.List;

import presage.Message;

public class TokenRequestAnswer extends Message implements HasTokens {

	public List<String> TokenList;
	
	public TokenRequestAnswer(String to, String from, String toKey,
			String fromKey, long timestamp, List<String> TokenList) {
		super(to, from, toKey, fromKey, "reply", "tokenrequest", timestamp);
		// TODO Auto-generated constructor stub
		this.TokenList = TokenList;
		
	}

	@Override
	public List<String> getTokens() {
		return TokenList;
	}

	/*@Override
	public List<String> getOwnTokens() {
		return TokenList;
	}*/

	@Override
	public void setTokens(List<String> tokens) {
		
	}

}
