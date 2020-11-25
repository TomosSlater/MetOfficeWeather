package training.metofficeweather;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class Forecast {
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
    private static final String[] weatherCodes = {
            "Clear night",
            "Sunny day",
            "Partly cloudy (night)",
            "Partly cloudy (day)",
            "Not used",
            "Mist",
            "Fog",
            "Cloudy",
            "Overcast",
            "Light rain shower (night)",
            "Light rain shower (day)",
            "Drizzle",
            "Light rain",
            "Heavy rain shower (night)",
            "Heavy rain shower (day)",
            "Heavy rain",
            "Sleet shower (night)",
            "Sleet shower (day)",
            "Sleet",
            "Hail shower (night)",
            "Hail shower (day)",
            "Hail",
            "Light snow shower (night)",
            "Light snow shower (day)",
            "Light snow",
            "Heavy snow shower (night)",
            "Heavy snow shower (day)",
            "Heavy snow",
            "Thunder shower (night)",
            "Thunder shower (day)",
            "Thunder}"};
    private int avgRainChanceNextDay;
    private int highestRainChanceNextDay;

    public Forecast(JsonNode jsonNode, String location) {
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

    public void printWeatherForecast() {
        System.out.println("The forecast for " + location + ":");
        System.out.println("Temperature: " + temperature + "°C (feels like: " + feelsLike + "°C)");
        System.out.println("Weather: " + getWeather());
        System.out.println("Visibility: " + getVisibility());
        System.out.println("Wind Speed: " + windSpeed + "mph " + windDirection + " with " + gustSpeed + "mph gusts");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Chance of precipitation: " + precipitationProbability + "%");
        System.out.println("UV strength: " + getUv());
    }

    public String getUv() {
        int uvNo = Integer.parseInt(uvCode);
        if (uvNo < 3) {
            return "Low. No protection required. You can safely stay outside.";
        } else if (uvNo < 6) {
            return "Moderate. Seek shade during midday hours, cover up and wear sunscreen.";
        } else if (uvNo < 8) {
            return "High. Seek shade during midday hours, cover up and wear sunscreen.";
        } else if (uvNo < 11) {
            return "Very high. Avoid being outside during midday hours. Shirt, sunscreen and hat are essential.";
        } else {
            return "Extreme. Avoid being outside during midday hours. Shirt, sunscreen and hat are essential.";
        }
    }

    public String getWeather() {
        if (weatherCode.matches("[0-9]+")) {
            int weatherInt = Integer.parseInt(weatherCode);
            if (weatherInt < 30) {
                return weatherCodes[weatherInt];
            }
        }
        return weatherCode;
    }

    public String getVisibility() {
        switch (visibilityCode) {
            case "UN":
                return "Unknown";
            case "VP":
                return "Very poor, less than 1 km";
            case "PO":
                return "Poor, between 1-4 km";
            case "MO":
                return "Moderate, between 4-10 km";
            case "GO":
                return "Good, between 10-20 km";
            case "VG":
                return "Very good, between 20-40 km";
            case "EX":
                return "Excellent, more than 40 km";
            default:
                return visibilityCode;
        }
    }

    public void setAvgRainChanceNextDay(int chance){
        avgRainChanceNextDay = chance;
    }

    public void setHighestRainChanceNextDay(int chance){
        highestRainChanceNextDay = chance;
    }
}
