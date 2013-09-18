package pl.ml4cast.downloader;

import org.joda.time.DateTime;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

public class CmdLineOptions {
	@Option(name = "-n", usage = "list of time series names (y:WIG etc)", handler = StringArrayOptionHandler.class)
	private String[] seriesNames;
	@Option(name = "-b", usage = "start of time period ", handler = DateOptionHandler.class)
	private DateTime startDate;
	@Option(name = "-e", usage = "end of time period ", handler = DateOptionHandler.class)
	private DateTime endDate;
	@Option(name = "-t", usage = "list of column types [price|volume]+", handler = StringArrayOptionHandler.class)
	private String[] columnTypes;
	@Option(name = "-dbh", usage = "database host")
	private String dbHost = "localhost";
	@Option(name = "-dbn", usage = "database name")
	private String dbName = "stocks";
	@Option(name = "-dbu", usage = "database user")
	private String dbUser = "root";
	@Option(name = "-dbP", usage = "database port")
	private Integer dbPort = 3306;
	@Option(name = "-dbp", usage = "database password")
	private String dbPassword = "";

	public String[] getSeriesNames() {
		return seriesNames;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public String[] getColumnTypes() {
		return columnTypes;
	}

	public String getDbHost() {
		return dbHost;
	}

	public String getDbName() {
		return dbName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public Integer getDbPort() {
		return dbPort;
	}

	public String getDbUser() {
		return dbUser;
	}
}
