package training.metofficeweather;

import com.fasterxml.jackson.databind.JsonNode;

public class Forcast {
    private final String location;
    private final String uvCode;
    private final String weatherCode;
    private final String visibilityCode;
    private final String temperature;
    private final String windSpeed;
    private final String precipitationProbability;
    private final String humidity;
    private final String gustSpeed;
    private final String feelsLike;
    private final String windDirection;


    public Forcast(JsonNode jsonNode, String location) {
        this.location = location;
        this.uvCode = jsonNode.get("U").asText();
        this.weatherCode = jsonNode.get("W").asText();
        this.visibilityCode = jsonNode.get("V").asText();
        this.temperature = jsonNode.get("T").asText();
        this.windSpeed = jsonNode.get("S").asText();
        this.precipitationProbability = jsonNode.get("Pp").asText();
        this.humidity = jsonNode.get("H").asText();
        this.gustSpeed = jsonNode.get("G").asText();
        this.feelsLike = jsonNode.get("F").asText();
        this.windDirection = jsonNode.get("D").asText();

    }

    public void printWeatherForcast() {
        System.out.println("The forcast for " + location + ":");
        System.out.println("Temperature: " + temperature + "°C (feels like: " + feelsLike + "°C)");
        System.out.println("Weather: " + weatherCode);
        System.out.println("Visibility: " + visibilityCode);
        System.out.println("Wind Speed: " + windSpeed + "mph " + windDirection + " with " + gustSpeed + "mph gusts");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Chance of precipitation: " + precipitationProbability + "%");
        System.out.println("UV strength: " + uvCode);
    }

}
