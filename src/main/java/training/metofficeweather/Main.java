package training.metofficeweather;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ForcastFetcher forcastFetcher = new ForcastFetcher("350734");
        Forcast forcast = forcastFetcher.getForcast();
        forcast.printWeatherForcast();
    }
}	
