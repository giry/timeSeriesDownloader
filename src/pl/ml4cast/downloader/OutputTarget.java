package pl.ml4cast.downloader;

import org.joda.time.DateTime;

public interface OutputTarget {
	void add(DateTime time, String name, double value);

	void close();
}
