package training.metofficeweather;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
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
