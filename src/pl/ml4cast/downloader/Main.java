package pl.ml4cast.downloader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.reflections.Reflections;

import pl.ml4cast.downloader.fetchers.YahooFetcher;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	private static final char SEPARATOR = ':';
	private static final String PREFIX = YahooFetcher.class.getPackage()
			.getName();
	private static HashMap<String, Fetcher> fetchers = new HashMap<>();

	public static void main(String[] args) {
		CmdLineOptions cmdLineOpts = parseArguments(args);
		if (cmdLineOpts == null)
			return;
		if (cmdLineOpts.getStartDate().isAfter(cmdLineOpts.getEndDate())) {
			System.err
					.println("Wrong arguments: start date is after end date.");
			return;
		}

		// ExecutorService executor = Executors.newFixedThreadPool(4);
		FetcherParams params = new FetcherParams(cmdLineOpts);
		Injector injector = Guice.createInjector(new MainModule(params));
		findFetchers(injector);

		String[] series = cmdLineOpts.getSeriesNames();
		for (String name : series) {
			int idx = name.indexOf(SEPARATOR);
			String prefix = name.substring(0, idx);
			String serie = name.substring(idx + 1);
			Fetcher fetcher = fetchers.get(prefix);
			if (fetcher != null) {
				fetcher.fetch(serie);
			}
		}
	}

	private static void findFetchers(Injector injector) {
		Reflections ref = new Reflections(PREFIX);
		Set<Class<? extends Fetcher>> fetcherClasses = ref
				.getSubTypesOf(Fetcher.class);
		Iterator<Class<? extends Fetcher>> it = fetcherClasses.iterator();
		while (it.hasNext()) {
			Class<? extends Fetcher> clazz = it.next();
			try {
				Fetcher fetcher = injector.getInstance(clazz);
				fetchers.put(fetcher.getName(), fetcher);
			} catch (SecurityException | IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	private static CmdLineOptions parseArguments(String[] args) {
		CmdLineOptions options = new CmdLineOptions();
		CmdLineParser parser = new CmdLineParser(options);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("java -jar prog.jar [options...]");
			parser.printUsage(System.err);
		}
		return options;
	}
}
