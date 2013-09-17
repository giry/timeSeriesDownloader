package pl.ml4cast.downloader.fetchers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import pl.ml4cast.downloader.ColumnTypes;
import pl.ml4cast.downloader.Fetcher;
import pl.ml4cast.downloader.FetcherParams;
import pl.ml4cast.downloader.OutputTarget;

public class YahooFetcher extends Fetcher {

	private static final String Y = "y";
	private static final String CSV_URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=%s&ignore=.csv";
	private static final String PERIOD = "d";

	public YahooFetcher(FetcherParams fetcherParams) {
		super(fetcherParams);
	}

	/**
	 * @see <a
	 *      href="http://code.google.com/p/yahoo-finance-managed/wiki/csvHistQuotesDownload">yahoo
	 *      finance csv api</a>
	 * 
	 */
	@Override
	public void fetch(String seriesName, OutputTarget target) {
		DateTime startTime = params.getStartTime();
		DateTime endTime = params.getEndTime();
		String strUrl = String.format(CSV_URL, seriesName,
				startTime.getMonthOfYear() - 1, startTime.getDayOfMonth(),
				startTime.getYear(), endTime.getMonthOfYear() - 1,
				endTime.getDayOfMonth(), endTime.getYear(), PERIOD);
		try {
			URL url = new URL(strUrl);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			parse(target, seriesName, reader);

		} catch (FileNotFoundException notFound) {
			System.err.println(seriesName + " not found");
		} catch (IOException e) {
			e.printStackTrace();
		}

		target.close();
	}

	private void parse(OutputTarget target, String name, BufferedReader reader)
			throws IOException {
		String dbName = getName() + name;
		String line;
		reader.readLine(); // omit header
		while ((line = reader.readLine()) != null) {
			String[] arr = line.split(",");
			DateTime time = DateTime.parse(arr[0],
					DateTimeFormat.forPattern("yyyy-MM-dd"));
			if (params.hasColumn(ColumnTypes.PRICE)) {
				float adjClose = Float.parseFloat(arr[6]);
				target.add(time, dbName, adjClose);
			}
			if (params.hasColumn(ColumnTypes.VOLUME)) {
				float volume = Float.parseFloat(arr[5]);
				target.add(time, dbName, volume);
			}

		}
	}

	@Override
	public String getName() {
		return Y;
	}

}
