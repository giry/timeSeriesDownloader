package pl.ml4cast.downloader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

public class DateOptionHandler extends OptionHandler<DateTime> {

	private static final String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss";

	public DateOptionHandler(CmdLineParser parser, OptionDef option,
			Setter<? super DateTime> setter) {
		super(parser, option, setter);
	}

	@Override
	public int parseArguments(Parameters params) throws CmdLineException {
		String strDate = params.getParameter(0);
		DateTime dateTime = DateTime.parse(strDate,
				DateTimeFormat.forPattern(DATE_FORMAT));
		if (dateTime == null) {
			throw new CmdLineException(owner, "Cannot parse date");
		}
		setter.addValue(dateTime);
		return 1;
	}

	@Override
	public String getDefaultMetaVariable() {
		return null;
	}

}
