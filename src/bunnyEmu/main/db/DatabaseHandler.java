package bunnyEmu.main.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bunnyEmu.main.Server;

public class DatabaseHandler {
	
	private static String authDB = "USE " + Server.prop.getProperty("authDB");
	//private static String charactersDB = "USE " + Server.prop.getProperty("charactersDB");
	//private static String worldDB = "USE " + Server.prop.getProperty("worldDB");
	
	/* this is to check that auth, characters, and world actually exist */
	public static boolean databasesExist(String auth, String characters, String world) throws SQLException {
		try {
			int databaseCounter = 0;
			Connection conn = DatabaseConnection.getConnection();

			ResultSet rst = conn.getMetaData().getCatalogs();

			// inefficient checks every database name on server 3 times
			while (rst.next()) {
				String dbName = rst.getString(1);
				
				if (dbName.equals(auth) || dbName.equals(characters) || dbName.equals(world)) {
					databaseCounter++;
				}
			}
			
			// cleanup connection
			DatabaseConnection.closeResultSet(rst);
			DatabaseConnection.closeConnection(conn);
			
			// means all 3 databases exist
			if (databaseCounter == 3) {
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/* creates an account in the authDB */
	public static boolean createAccount(String userName, String hashPW) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();

			/* try to add an account here from userName and password hash */
			st.execute(authDB);
			st.executeUpdate("INSERT INTO `account` (`username`, `hashPW`) VALUES (" + 
														"'" + userName + "', '" + hashPW + "');");

			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeConnection(conn);

			return true;
		}
		// this will throw duplicate errors because of Unique constraint
		// just silence the error and red text and tell client creation failed
		catch (SQLException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	// server console command -- this is hacky needs to be rewritten "for niceness"
	public static void queryOnline() throws SQLException {
		int counter = 0;
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();

		ResultSet rst = st.executeQuery("Select * from players where online = 1;");

		System.out.print("{ ");

		/* id, name, level, class, x-pos, y-pos, online */
		while (rst.next()) {
			System.out.print(rst.getString(2) + " ");
			counter++;
		}

		// no online players
		if (counter == 0) {
			System.out.print("NONE ");
		}

		System.out.print("}");

		// cleanup connection
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeResultSet(rst);
		DatabaseConnection.closeConnection(conn);
	}

	// init everyone to offline
	public static void turnAllOffline() throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();

		st.executeUpdate("UPDATE auth.accounts SET ONLINE = 0 WHERE 1");
		st.executeUpdate("UPDATE character.players SET ONLINE = 0 WHERE 1");
		
		// cleanup
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeConnection(conn);
	}

	public static String[] queryAuth(String username) {
		try {
			int ID = 0;
			String[] userInfo = new String[6];

			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			ResultSet rst = st.executeQuery("SELECT * FROM auth.accounts WHERE Username = '" + username + "'");

			/* ID, Username, Password (Hashed) */
			if (rst.next()) {
				ID = rst.getInt("ID");
				userInfo[0] = String.valueOf(ID);
				
				// I think this is where password should be returned to be checked
				userInfo[1] = rst.getString("Password");

				/* TODO: this part should happen after authentication to populate CMSG_CHAR_ENUM */
				rst = st.executeQuery("SELECT * FROM characters.players WHERE accountID = " + ID);

				// return online player by populating an array -- TODO: update this to current schema
				if (rst.next()) {
					userInfo[2] = rst.getString(2);
					userInfo[3] = String.valueOf(rst.getInt(5));
					userInfo[4] = String.valueOf(rst.getInt(6));
				}
			}

			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeResultSet(rst);
			DatabaseConnection.closeConnection(conn);

			return userInfo;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void turnOnline(int ID) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();

			st.executeUpdate("UPDATE auth.accounts SET ONLINE = 1 WHERE Id = " + ID);
			
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeConnection(conn);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// turn an account offline
	public static void removeOnline(int accountID, int position) throws SQLException {
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate("UPDATE auth.accounts SET Online = 0 WHERE Id = " + accountID);
			// cleanup -- important
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeConnection(conn);
		} 
		catch (Exception e) {
			System.out.println("Acount " + accountID + "could not be set to offline.");
			e.printStackTrace();
		}
	}

	// update player's coordinates here
	public static void updateCoordinates(int id, float x, float y, float z, float O) throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();
		st.executeUpdate("UPDATE characters.players SET `X-Pos` = " + x + "," + " " + 
							"`Y-Pos` = " + y + "," + "`Z-Pos` = " + z + "`O-Pos` = " + O + 
							" WHERE ID = " + id);

		// cleanup
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeConnection(conn);
	}

	
	/** world queries -- long way from implementing these **/
	
	/* add all the creature spawns to send to clients */
	public static void queryCreatures() {
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			ResultSet rst = st.executeQuery("SELECT * FROM world.creature_spawns;");

			while (rst.next()) {
				// add creatures here
			}

			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeResultSet(rst);
			DatabaseConnection.closeConnection(conn);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}