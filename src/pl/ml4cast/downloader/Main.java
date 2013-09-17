package pl.ml4cast.downloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.joda.time.DateTime;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.reflections.Reflections;

import pl.ml4cast.downloader.fetchers.YahooFetcher;

public class Main {
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
		findFetchers(params);
		OutputTarget target = new OutputTarget() {

			@Override
			public void close() {
				// TODO Auto-generated method stub

			}

			@Override
			public void add(DateTime time, String name, double value) {
				System.out.println(name + " " + value);

			}
		};
		String[] series = cmdLineOpts.getSeriesNames();
		for (String name : series) {
			int idx = name.indexOf(':');
			String prefix = name.substring(0, idx);
			String serie = name.substring(idx + 1);
			Fetcher fetcher = fetchers.get(prefix);
			if (fetcher != null) {
				fetcher.fetch(serie, target);
			}
		}
	}

	private static void findFetchers(FetcherParams params) {
		Reflections ref = new Reflections(PREFIX);
		Set<Class<? extends Fetcher>> fetcherClasses = ref
				.getSubTypesOf(Fetcher.class);
		Iterator<Class<? extends Fetcher>> it = fetcherClasses.iterator();
		while (it.hasNext()) {
			Class<? extends Fetcher> clazz = it.next();
			try {
				Constructor<? extends Fetcher> ctor = clazz
						.getConstructor(FetcherParams.class);
				if (ctor != null) {
					Fetcher fetcher = ctor.newInstance(params);
					fetchers.put(fetcher.getName(), fetcher);
				}
			} catch (NoSuchMethodException | SecurityException
					| InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace(); // TODO co zrobic z wyjatkiem?
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
