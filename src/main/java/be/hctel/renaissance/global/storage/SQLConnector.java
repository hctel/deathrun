package be.hctel.renaissance.global.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnector {
	private String username, host, database, password;
	private int port;
	private long maxTimeoutMillis, lastActive;
	
	private Connection con;
	
	public SQLConnector(String username, String host, int port, String database, String password) {
		this(username, host, port, database, password, 3600*1000L);
	}
	
	public SQLConnector(String username, String host, int port, String database, String password, long maxTimeoutMillis) {
		this.username = username;
		this.host = host;
		this.port = port;
		this.database = database;
		this.password = password;
		this.maxTimeoutMillis = maxTimeoutMillis;
		checkConnection();
	}
	
	public void execute(String SQLCommand) throws SQLException {
		checkConnection();
		Statement st = con.createStatement();
		st.execute(SQLCommand);
	}
	
	public ResultSet executeQuery(String SQLCommand) throws SQLException {
		checkConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(SQLCommand);
		return rs;
	}
	
	private void checkConnection() {
		if(System.currentTimeMillis() - lastActive > maxTimeoutMillis) createConnection();
	}
	
	private void createConnection() {
		synchronized (this) {
	        try {
				if (con != null && !con.isClosed()) {
				    return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true", username, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}
