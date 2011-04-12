package structures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiRun {
	
	public static void main(String[] args) {
		
		class Sim implements Runnable {

			String scenario;
			int simID;
			int paramSet;
			double connectionProb;
			double disconnectionProb;
			
			public Sim(String scenario, int simID, int paramSet,
					double connectionProb, double disconnectionProb) {
				super();
				this.scenario = scenario;
				this.simID = simID;
				this.paramSet = paramSet;
				this.connectionProb = connectionProb;
				this.disconnectionProb = disconnectionProb;
			}

			@Override
			public void run() {
				try {
					HeadlessRun.main(new String[] { scenario, 
							String.valueOf(simID), 
							String.valueOf(paramSet), 
							String.valueOf(connectionProb), 
							String.valueOf(disconnectionProb)});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		Connection conn = null;
		ExecutorService pool = null;
		try {
			// database connection
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/structures", "presage", "C5xd9feYcJbw3hy9");
		
			// thread pool
			pool = Executors.newFixedThreadPool(6);
			
			Statement st = conn.createStatement();
			st.execute("SELECT s.id, s.scenario, s.paramSet, p.connectionProb, p.disconnectionProb " +
					"FROM simulations AS s " +
					"JOIN paramSets as p ON p.id = s.paramSet " +
					"WHERE s.timestamp IS NULL " +
					"ORDER BY s.id ASC");
			ResultSet r = st.getResultSet();
			
			while(r.next()) {
				int simID = r.getInt(1);
				String scenario = r.getString(2);
				int paramSet = r.getInt(3);
				double cp = r.getDouble(4);
				double dp = r.getDouble(5);
				System.out.println("Running simulation #"+simID+": ("+scenario+", "+paramSet+", "+cp+", "+dp+")");
				pool.submit(new Sim(scenario, simID, paramSet, cp, dp));
				//pool.execute(new Sim(scenario, simID, paramSet, cp, dp));
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pool.shutdown();
		}
		
	}
	
}
