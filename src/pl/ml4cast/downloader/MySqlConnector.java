package pl.ml4cast.downloader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MySqlConnector {

	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private String url;

	@Inject
	public MySqlConnector(CmdLineOptions cmdLineOptions)
			throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER_CLASS_NAME);
		url = String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
				cmdLineOptions.getDbHost(), cmdLineOptions.getDbName(),
				cmdLineOptions.getDbUser(), cmdLineOptions.getDbPassword());
		Connection con = DriverManager.getConnection(url);
		con.close();
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}

}
