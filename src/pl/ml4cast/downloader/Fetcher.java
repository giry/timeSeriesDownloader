package pl.ml4cast.downloader;

public abstract class Fetcher {

	protected FetcherParams params;

	protected Fetcher(FetcherParams fetcherParams) {
		this.params = fetcherParams;
	}

	public abstract void fetch(String seriesName);

	public abstract String getName();
}
