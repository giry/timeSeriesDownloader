package pl.ml4cast.downloader.tests;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import pl.ml4cast.downloader.FetcherParams;
import pl.ml4cast.downloader.fetchers.YahooFetcher;

public class TestYahooFetcher {

	@Test
	public void testParse() {
		// given
		FetcherParams fetcherParams = mock(FetcherParams.class);
		YahooFetcher fetcher = new YahooFetcher(fetcherParams);
		// when

		// then
	}

}
