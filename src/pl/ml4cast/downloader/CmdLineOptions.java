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

}
