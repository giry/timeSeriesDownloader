package pl.ml4cast.downloader;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Locale;

import org.joda.time.DateTime;

public class FetcherParams {
	private final DateTime startTime;
	private final DateTime endTime;
	private final EnumSet<ColumnTypes> columns;

	public FetcherParams(CmdLineOptions cmdLineOptions) {
		startTime = cmdLineOptions.getStartDate();
		endTime = cmdLineOptions.getEndDate();
		ArrayList<ColumnTypes> typesList = new ArrayList<>();
		for (String typeStr : cmdLineOptions.getColumnTypes()) {
			ColumnTypes type = ColumnTypes.valueOf(typeStr
					.toUpperCase(Locale.US));
			typesList.add(type);
		}
		columns = EnumSet.copyOf(typesList);
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public boolean hasColumn(ColumnTypes col) {
		return columns.contains(col);
	}
}
