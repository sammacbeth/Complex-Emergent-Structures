package structures;

import java.util.ArrayList;

import presage.Input;

public class TokensInput implements Input {

	private long timestamp;
	private String performative = "tokens";
	private ArrayList<String> tokens = new ArrayList<String>();
	
	public TokensInput(long timestamp, ArrayList<String> tokens) {
		super();
		this.timestamp = timestamp;
		this.tokens = tokens;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getPerformative() {
		return performative;
	}
	
	public ArrayList<String> getTokens() {
		return tokens;
	}

}
