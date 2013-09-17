package pl.ml4cast.downloader;

import org.joda.time.DateTime;

public class FetcherParams {
	private final String seriesName;
	private final DateTime startTime;
	private final DateTime endTime;

	public FetcherParams(String seriesName, DateTime startTime, DateTime endTime) {
		this.seriesName = seriesName;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}
}
