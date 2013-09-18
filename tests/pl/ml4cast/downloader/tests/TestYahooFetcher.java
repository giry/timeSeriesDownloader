package pl.ml4cast.downloader.tests;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.StringReader;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

import pl.ml4cast.downloader.ColumnTypes;
import pl.ml4cast.downloader.FetcherParams;
import pl.ml4cast.downloader.OutputTarget;
import pl.ml4cast.downloader.UrlReader;
import pl.ml4cast.downloader.fetchers.YahooFetcher;

public class TestYahooFetcher {

	@Test
	public void testFetch() {
		// given
		FetcherParams fetcherParams = mock(FetcherParams.class);
		OutputTarget target = mock(OutputTarget.class);
		UrlReader urlReader = mock(UrlReader.class);
		DateTime start = new DateTime(2010, 1, 7, 0, 0);
		DateTime end = new DateTime(2010, 1, 19, 0, 0);
		String rightUrl = "http://ichart.yahoo.com/table.csv?s=GOOG&a=0&b=7&c=2010&d=0&e=19&f=2010&g=d&ignore=.csv";
		when(fetcherParams.getStartTime()).thenReturn(start);
		when(fetcherParams.getEndTime()).thenReturn(end);
		when(fetcherParams.hasColumn(any(ColumnTypes.class))).thenReturn(true);
		String response = "Date,Open,High,Low,Close,Volume,Adj Close\n"
				+ "2010-01-25,546.59,549.88,525.61,529.94,4021800,529.94\n"
				+ "2010-01-19,581.20,590.42,534.86,550.01,5168800,550.01\n"
				+ "2010-01-11,604.46,604.46,573.90,580.00,5647400,580.00\n"
				+ "2010-01-04,626.95,629.51,589.11,602.02,4015600,602.02\n";
		BufferedReader reader = new BufferedReader(new StringReader(response));
		when(urlReader.read(rightUrl)).thenReturn(reader);
		YahooFetcher fetcher = new YahooFetcher(fetcherParams, target,
				urlReader);
		// when
		fetcher.fetch("GOOG");
		// then
		verify(target).add(eq(new DateTime(2010, 1, 19, 0, 0)), eq("yGOOG"),
				AdditionalMatchers.eq(550.01, 0.0001));
		verify(target).add(eq(new DateTime(2010, 1, 19, 0, 0)), eq("yGOOG"),
				AdditionalMatchers.eq(5168800, 0.0001));
		verify(target).add(eq(new DateTime(2010, 1, 11, 0, 0)), eq("yGOOG"),
				AdditionalMatchers.eq(580.00, 0.0001));
		verify(target).close();
	}
}
