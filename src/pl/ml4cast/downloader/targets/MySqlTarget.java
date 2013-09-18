package pl.ml4cast.downloader.targets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import pl.ml4cast.downloader.MySqlConnector;
import pl.ml4cast.downloader.OutputTarget;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MySqlTarget implements OutputTarget {

	private static final String INSERT_SERIES_ROW = "INSERT IGNORE INTO series(name, value, time) VALUES (?,?,?)";
	private MySqlConnector connector;
	private List<Row> buffer = new ArrayList<>();

	@Inject
	public MySqlTarget(MySqlConnector connector) {
		this.connector = connector;
	}

	@Override
	public void add(DateTime time, String name, double value) {
		buffer.add(new Row(time, name, value));
	}

	@Override
	public void close() {
		try (Connection con = connector.getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(INSERT_SERIES_ROW)) {
				for (Row row : buffer) {
					ps.setString(1, row.name);
					ps.setDouble(2, row.value);
					ps.setTimestamp(3, new Timestamp(row.time.getMillis()));
					ps.addBatch();
					ps.clearParameters();
				}
				ps.executeBatch();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private class Row {
		public final DateTime time;
		public final double value;
		public final String name;

		public Row(DateTime time, String name, double value) {
			this.time = time;
			this.name = name;
			this.value = value;
		}
	}

}
