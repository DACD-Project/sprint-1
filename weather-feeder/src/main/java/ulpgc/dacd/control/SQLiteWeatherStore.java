package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather.ForecastEntry;

import java.sql.*;

public class SQLiteWeatherStore implements WeatherStore {
    private final String dbPath;

    public SQLiteWeatherStore(String dbPath) {
        this.dbPath = dbPath;
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "CREATE TABLE IF NOT EXISTS weather (timestamp INTEGER PRIMARY KEY, temperature REAL, humidity INTEGER, date_time TEXT)";
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeWeather(ForecastEntry weather) {
        try (Connection conn = DriverManager.getConnection(dbPath);
             PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM weather WHERE timestamp = ? AND temperature = ? AND humidity = ?");
             PreparedStatement insertStmt = conn.prepareStatement("INSERT OR REPLACE INTO weather (timestamp, temperature, humidity, date_time) VALUES (?, ?, ?, ?)")
        ) {
            checkStmt.setLong(1, weather.getTimestamp());
            checkStmt.setDouble(2, weather.getTemperature());
            checkStmt.setInt(3, weather.getHumidity());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                insertStmt.setLong(1, weather.getTimestamp());
                insertStmt.setDouble(2, weather.getTemperature());
                insertStmt.setInt(3, weather.getHumidity());
                insertStmt.setString(4, weather.getDateTime());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
