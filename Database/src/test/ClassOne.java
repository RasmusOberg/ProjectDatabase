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
//		class1.getActsFromStage("Kojan");
//		class1.getBand("Stora scen", "16:00");
//		class1.getStage("Teddybears", "16:00", "Torsdag");
//		class1.getBandOnStage("16:00", "Torsdag");
//		class1.getStageAndTime("Teddybears");
//		class1.addNewBand("Teddybears", "Sweden", 5);
//		class1.getallworkers();
//		class1.addWorker("rasmus", "9090", "Nygatan");
//		class1.addBandMember("Fredrik", "Född: 1997, Land: USA");
//		class1.addMemberToBand("Fredrik", "Teddybears");
//		class1.addAct("Kojan", "Teddybears", "16:00", "18:00", "Lördag");
		class1.getAllActs();
	}

	// Connect to db
	public Connection connect(){
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

	// Stage + band --> time
	public void getTime(String stagename, String bandname) {
		String SQL = "SELECT act.start, act.endtime, act.date\n" + "FROM act\n" + "WHERE bandid =\n"
				+ "      (SELECT bands.id FROM bands WHERE bandname = '" + bandname + "')\n" + "  AND stageid =\n"
				+ "      (SELECT stages.id FROM stages WHERE stages.name = '" + stagename + "');";

		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while (rs.next()) {
				System.out.println(rs.getString("start") + rs.getString("endtime") + rs.getString("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Stage --> bands + times
	public void getActsFromStage(String stagename) {
		String SQL = "SELECT bands.bandname, act.starttime, act.endtime, act.date"
				+ "FROM act\n"
				+ "INNER JOIN bands"
				+ "ON act.bandid = bands.id" 
				+ "WHERE stageid ="
					+ "(SELECT stages.id" 
						+ "FROM stages\n"
						+ "WHERE stages.name = '" + stagename + "');";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while (rs.next()) {
				System.out.println(rs.getString("bandname") + rs.getString("starttime") + rs.getString("endtime")
						+ rs.getString("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Time+band --> stage
	public void getStage(String band, String starttime, String day) {
		String SQL = "SELECT stages.name\n" + "FROM stages\n" + "INNER JOIN act ON stages.id = act.stageid\n"
				+ "    WHERE act.bandid = (SELECT bands.id FROM bands WHERE bands.bandname = '" + band + "')\n"
				+ "        AND act.start = '" + starttime + "'\n" + "        AND act.date = '" + day + "';";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while (rs.next()) {
				System.out.println(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Time --> band + stage
	public void getBandOnStage(String starttime, String day) {
		String SQL = "SELECT bands.bandname, stages.name\n" + "    FROM act\n"
				+ "        INNER JOIN stages ON act.stageid = stages.id\n"
				+ "        INNER JOIN bands ON act.bandid = bands.id\n" + "    WHERE act.start = '" + starttime + "'\n"
				+ "            AND act.date = '" + day + "';";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while (rs.next()) {
				System.out.println(rs.getString("bandname") + rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// stage+time --> band
	public void getBand(String stagename, String starttime) {
		String SQL = "SELECT bands.bandname\n" + "from bands\n" + "INNER JOIN act ON bands.id = act.bandid\n"
				+ "    where act.stageid = (SELECT stages.id FROM stages WHERE stages.name = '" + stagename + "')\n"
				+ "        AND act.start = '16:00'\n" + "        AND act.date = 'Torsdag';";

		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while (rs.next()) {
				System.out.println(rs.getString("bandname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Bandname --> stage + time
	public void getStageAndTime(String bandname) {
		String SQL = "SELECT stages.name, act.start, act.endtime, act.date\n" + "    FROM act\n"
				+ "        INNER JOIN stages ON act.stageid = stages.id\n" + "    WHERE act.bandid = (\n"
				+ "                        SELECT bands.id\n" + "                        FROM bands\n"
				+ "                        WHERE bands.bandname = '" + bandname + "');";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while (rs.next()) {
				System.out.println(
						rs.getString("name") + rs.getString("start") + rs.getString("endtime") + rs.getString("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Test
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
	
	//adds a new band
	public void addNewBand(String name, String country, int contactperson) {
		String SQL = "INSERT INTO bands (bandname, country, contactperson) VALUES('" + name + "', '" + country + "', " + contactperson + ");";
		System.out.println("Inside addnewband");
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				){
		stmt.executeUpdate(SQL);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("FUCK");
		}
	}
	
	//adds a new worker
	public void addWorker(String name, String ssn, String address) {
		String SQL = "INSERT INTO workers (name, ssn, address) VALUES ('" + name + "', '" + ssn + "', '" + address + "');";
		
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				){
			stmt.executeUpdate(SQL);
			System.out.println("Added " + name);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("FUCK");
		}
	}
	
	//add bandmember
	public void addBandMember(String name, String info) {
		String SQL = "INSERT INTO members (name, info) VALUES ('" + name + "', '" + info + "');";
		
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				){
			stmt.executeUpdate(SQL);
			System.out.println("Added " + name);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("FUCK");
		}
	}
	
	//add a member to a band
	public void addMemberToBand(String membername, String bandname) {
		String SQL = "INSERT INTO membersInBand VALUES("
				+ "(SELECT bands.id "
				+ "FROM bands "
				+ "WHERE bands.bandname = '" + bandname + "'),"
				+ "(SELECT members.id "
				+ "FROM members "
				+ "WHERE members.name = '" + membername + "'))";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				){
			stmt.executeUpdate(SQL);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("FUCK");
		}
	}
	
	//add new act
	public void addAct(String stagename, String bandname, String starttime, String endtime, String date) {
		String SQL = "INSERT INTO act VALUES(\n" + 
				"(SELECT stages.id FROM stages WHERE stages.name = '" + stagename +"')," + 
				"(SELECT bands.id FROM bands WHERE bands.bandname = '" + bandname + "')," + 
				"'" + starttime + "'," + 
				"'" + endtime + "'," + 
				"'" + date + "');";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				){
			stmt.executeUpdate(SQL);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("FUCK");
		}
	}
	
	public void getallworkers() {
		String SQL = "SELECT * FROM workers";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while(rs.next()) {
				System.out.println(rs.getString("name") + rs.getString("id"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void getAllActs() {
		String SQL = "SELECT stages.name, bands.bandname, act.starttime, act.endtime, act.date " + 
				"FROM act " + 
				"INNER JOIN bands " + 
				"ON bands.id = act.bandid " + 
				"INNER JOIN stages " + 
				"ON stages.id = act.stageid;";
		try (Connection conn1 = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL)) {
			while(rs.next()) {
				
				System.out.println(rs.getString("name") + " "
						+ rs.getString("bandname")+ " "
						+ rs.getString("starttime")+ " "
						+ rs.getString("endtime")+ " "
						+ rs.getString("date"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
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
