package structures;

public class Force {

	String participantId;
	double x;
	double y;
	
	public Force(String pid, double x, double y) {
		super();
		this.participantId = pid;
		this.x = x;
		this.y = y;
	}
	
	public static Force fromMove(Move m) {
		return new Force(m.getParticipantId(), m.moveX, m.moveY);
	}
	
	public void add(Force f) {
		this.x += f.x;
		this.y += f.y;
	}
	
	public String getParticipantId() {
		return participantId;
	}

	public static Force fromPolar(double mag, double angle) {
		return new Force(null, mag * Math.cos(angle), mag * Math.sin(angle));
	}

	public Move toMove() {
		return new Move(participantId, (int) Math.rint(x), (int) Math.rint(y));
	}

	public double getMagnitude() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
}
