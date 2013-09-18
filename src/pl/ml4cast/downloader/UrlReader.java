package pl.ml4cast.downloader;

import java.io.BufferedReader;

public interface UrlReader {
	BufferedReader read(String url);
}
