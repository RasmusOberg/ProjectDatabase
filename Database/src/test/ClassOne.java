package test;

import java.sql.*;

public class ClassOne {
	private Connection conn = null;
	String SQL = "";

	public static void main(String[] args) {
		ClassOne class1 = new ClassOne();
//		class1.getAllBands("bandname");
//		class1.showAct();
//		class1.getTime("Stora scen", "Teddybears");
//		class1.getAllActs("Stora scen");
//		class1.getBand("Stora scen", "16:00");
//		class1.getStage("Teddybears", "16:00", "Torsdag");
//		class1.getBandOnStage("16:00", "Torsdag");
		class1.getStageAndTime("Teddybears");
	}

	public Connection connect() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String url = "jdbc:postgresql://localhost/mortfors";
		String user = "rasmusoberg";
		String password = "fzLtBWF3Qxb2juq";
		
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
	

	public void getTime(String stagename, String bandname) {
		String SQL = "SELECT act.start, act.endtime, act.date\n" + 
				"FROM act\n" + 
				"WHERE bandid =\n" + 
				"      (SELECT bands.id FROM bands WHERE bandname = '" + bandname + "')\n" + 
				"  AND stageid =\n" + 
				"      (SELECT stages.id FROM stages WHERE stages.name = '" + stagename + "');";
		
		try(Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)){
			while(rs.next()) {
				System.out.println(rs.getString("start") + rs.getString("endtime") + rs.getString("date"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getAllActs(String stagename) {
		String SQL = "SELECT bands.bandname, act.start, act.endtime, act.date\n" + 
				"      FROM act\n" + 
				"        INNER JOIN bands\n" + 
				"         ON act.bandid = bands.id\n" + 
				"      WHERE stageid =\n" + 
				"                    (SELECT stages.id\n" + 
				"                    FROM stages\n" + 
				"                    WHERE stages.name = '" + stagename + "');";
		try(Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)){
			while(rs.next()) {
				System.out.println(rs.getString("bandname") + rs.getString("start") +
						rs.getString("endtime") + rs.getString("date"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getStage(String band, String starttime, String day) {
		String SQL = "SELECT stages.name\n" + 
				"FROM stages\n" + 
				"INNER JOIN act ON stages.id = act.stageid\n" + 
				"    WHERE act.bandid = (SELECT bands.id FROM bands WHERE bands.bandname = '" + band + "')\n" + 
				"        AND act.start = '" + starttime + "'\n" + 
				"        AND act.date = '" + day + "';";
		try(Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)){
			while(rs.next()) {
				System.out.println(rs.getString("name"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getBandOnStage(String starttime, String day) {
		String SQL = "SELECT bands.bandname, stages.name\n" + 
				"    FROM act\n" + 
				"        INNER JOIN stages ON act.stageid = stages.id\n" + 
				"        INNER JOIN bands ON act.bandid = bands.id\n" + 
				"    WHERE act.start = '" + starttime + "'\n" + 
				"            AND act.date = '" + day + "';";
		try(Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)){
			while(rs.next()) {
				System.out.println(rs.getString("bandname") + rs.getString("name"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getBand(String stagename, String starttime) {
		String SQL = "SELECT bands.bandname\n" + 
				"from bands\n" + 
				"INNER JOIN act ON bands.id = act.bandid\n" + 
				"    where act.stageid = (SELECT stages.id FROM stages WHERE stages.name = '" + stagename + "')\n" + 
				"        AND act.start = '16:00'\n" + 
				"        AND act.date = 'Torsdag';";
		
		try(Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)){
			while(rs.next()) {
				System.out.println(rs.getString("bandname"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void getAllBands(String str) {
		String SQL = "SELECT " + str + " FROM bands";
		System.out.println("1");

		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			displayBands(rs, str);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void getStageAndTime(String bandname) {
		String SQL = "SELECT stages.name, act.start, act.endtime, act.date\n" + 
				"    FROM act\n" + 
				"        INNER JOIN stages ON act.stageid = stages.id\n" + 
				"    WHERE act.bandid = (\n" + 
				"                        SELECT bands.id\n" + 
				"                        FROM bands\n" + 
				"                        WHERE bands.bandname = '" + bandname + "');";
		try(Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)){
			while(rs.next()) {
				System.out.println(rs.getString("name") + rs.getString("start") + rs.getString("endtime") + rs.getString("date"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private void displayBands(ResultSet rs, String str) throws SQLException {
		while (rs.next()) {
			System.out.println(rs.getString(str));
		}
	}
	
	private void displayBand(ResultSet rs, String str) throws SQLException {
		while (rs.next()) {
			System.out.println(rs.getString(str));
		}
	}

}
