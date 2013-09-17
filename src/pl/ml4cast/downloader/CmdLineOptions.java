package pl.ml4cast.downloader;

import org.joda.time.DateTime;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

public class CmdLineOptions {
	@Option(name = "-s", usage = "data source [bossa|yahoo]")
	private String dataSource;
	@Option(name = "-n", usage = "list of time series names (WIG etc)", handler = StringArrayOptionHandler.class)
	private String[] seriesNames;
	@Option(name = "-b", usage = "start of time period ", handler = DateOptionHandler.class)
	private DateTime startDate;

	public String getDataSource() {
		return dataSource;
	}

	public String[] getSeriesNames() {
		return seriesNames;
	}
}
