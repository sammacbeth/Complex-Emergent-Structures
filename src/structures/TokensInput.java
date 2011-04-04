package structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashSet;
import java.util.Set;

import presage.Input;

public class TokensInput implements Input {

	private long timestamp;
	private String performative = "tokens";
	private ArrayList<String> tokens = new ArrayList<String>();
	private Set<String> slaves = new HashSet<String>();
	
	public TokensInput(long timestamp, ArrayList<String> tokens, Set<String> slaves) {
		super();
		this.timestamp = timestamp;
		this.tokens = tokens;
		this.slaves = slaves;
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
	
	public Set<String> getSlaves() {
		return slaves;
	}

}
