package training.metofficeweather;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TomosTesting {

    public static void main(String[] args) {
        APIReader reader = new APIReader();

        try {
            Locations locations = reader.getLocations();
            ConsoleApplication console = new ConsoleApplication(locations);
            console.runApplication();

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
