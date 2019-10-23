package test;

import java.sql.*;

public class ClassOne {
	private Connection conn = null;

	public static void main(String[] args) {
		ClassOne class1 = new ClassOne();
		class1.getId();
	}

	public Connection connect() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String url = "jdbc:postgresql://localhost/postgres";
		String user = "rasmusoberg";
		String password = "Mbirjr94!";
		
//		String url = "jdbc:postgresql://pgserver.mah.se:80/AE7610";
//		String user ="AE7610";
//		String password ="tnoogmwp";
		
		
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}

	public void getId() {
		String SQL = "SELECT * FROM Employee";
		System.out.println("1");

		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			displayActor(rs);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void displayActor(ResultSet rs) throws SQLException {
		while (rs.next()) {
			System.out.println(rs.getString("first_name"));
		}
	}

	public int getActorCounts() {
		String SQL = "SELECT count(*) FROM Employee";
		int count = 0;

		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return count;
	}

}
