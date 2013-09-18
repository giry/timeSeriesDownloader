package pl.ml4cast.downloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import pl.ml4cast.downloader.targets.MySqlTarget;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class MainModule extends AbstractModule {

	private FetcherParams fetcherParams;
	private UrlReader urlReader;
	private OutputTarget outputTarget;

	public MainModule(FetcherParams fetcherParams) {
		this.fetcherParams = fetcherParams;
		urlReader = new UrlReader() {
			@Override
			public BufferedReader read(String strUrl) {
				URL url;
				BufferedReader reader = null;
				try {
					url = new URL(strUrl);
					reader = new BufferedReader(new InputStreamReader(
							url.openStream()));
				} catch (FileNotFoundException notFound) {
					System.err.println("Can't download " + strUrl);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return reader;
			}
		};

	}

	@Override
	protected void configure() {
		bind(OutputTarget.class).to(MySqlTarget.class);
	}

	@Provides
	public FetcherParams provideFetcherParams() {
		return fetcherParams;
	}

	@Provides
	public UrlReader provideUrlReader() {
		return urlReader;
	}

}
