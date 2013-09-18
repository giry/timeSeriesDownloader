package pl.ml4cast.downloader.fetchers;

import java.io.BufferedReader;
import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import pl.ml4cast.downloader.ColumnTypes;
import pl.ml4cast.downloader.Fetcher;
import pl.ml4cast.downloader.FetcherParams;
import pl.ml4cast.downloader.OutputTarget;
import pl.ml4cast.downloader.UrlReader;

import com.google.inject.Inject;

public class YahooFetcher extends Fetcher {

	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String PREFIX = "y";
	private static final String CSV_URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=%s&ignore=.csv";
	private static final String PERIOD = "d";
	private final OutputTarget target;
	private final UrlReader urlReader;

	@Inject
	public YahooFetcher(FetcherParams fetcherParams, OutputTarget target,
			UrlReader urlReader) {
		super(fetcherParams);
		this.target = target;
		this.urlReader = urlReader;
	}

	/**
	 * @see <a href="http://code.google.com/p/yahoo-finance-managed/wiki/csvHistQuotesDownload">yahoo finance csv api</a>
	 * 
	 */
	@Override
	public void fetch(String seriesName) {
		DateTime startTime = params.getStartTime();
		DateTime endTime = params.getEndTime();
		String strUrl = String.format(CSV_URL, seriesName,
				startTime.getMonthOfYear() - 1, startTime.getDayOfMonth(),
				startTime.getYear(), endTime.getMonthOfYear() - 1,
				endTime.getDayOfMonth(), endTime.getYear(), PERIOD);

		BufferedReader reader = urlReader.read(strUrl);
		if (reader != null) {
			parse(target, seriesName, reader);
		}

		target.close();
	}

	private void parse(OutputTarget target, String name, BufferedReader reader) {
		String dbNamePrice = getName() + name + "_price";
		String dbNameVolume = getName() + name + "_volume";
		String line;
		try {
			reader.readLine();// omit header

			while ((line = reader.readLine()) != null) {
				String[] arr = line.split(",");
				DateTime time = DateTime.parse(arr[0],
						DateTimeFormat.forPattern(DATE_PATTERN));
				if (time.isAfter(params.getEndTime())) {
					continue;
				}
				if (time.isBefore(params.getStartTime()))
					break;
				if (params.hasColumn(ColumnTypes.PRICE)) {
					float adjClose = Float.parseFloat(arr[6]);
					target.add(time, dbNamePrice, adjClose);
				}
				if (params.hasColumn(ColumnTypes.VOLUME)) {
					float volume = Float.parseFloat(arr[5]);
					target.add(time, dbNameVolume, volume);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return PREFIX;
	}

}
