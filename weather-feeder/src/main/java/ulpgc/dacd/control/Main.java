package ulpgc.dacd.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        WeatherProvider provider = new OpenWeatherMapProvider();
        WeatherStore store = new SQLiteWeatherStore();
        WeatherController controller = new WeatherController(provider, store);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> controller.execute(40.4168, -3.7038), 0, 6, TimeUnit.HOURS);
    }
}