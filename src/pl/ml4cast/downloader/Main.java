package pl.ml4cast.downloader;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Set;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.reflections.Reflections;

public class Main {
	private static final String PREFIX = "pl.ml4cast.downloader.fetchers";

	public static void main(String[] args) {
		CmdLineOptions opts = parseArguments(args);

		// ExecutorService executor = Executors.newFixedThreadPool(4);
		// findFetchers();

	}

	private static void findFetchers() {
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
					// ctor.newInstance();
				}
			} catch (NoSuchMethodException | SecurityException e) {
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
