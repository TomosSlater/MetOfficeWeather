package training.metofficeweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class APIReader {

    @JsonProperty(value = "Locations")
    private final Client client = ClientBuilder.newClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey = System.getenv("MET_KEY");
    private int forecastsPerDay = 1;

    public Locations getLocations() {
        String inputData = client.target("http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key="
                + apiKey)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        try {
            return objectMapper.readValue(inputData, Root.class).getLocations();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JsonNode getJsonFromUrl(String url) {
        String json = client.target(url)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Forecast getForecast(String locationId) {
        List<Forecast> forecastsToday = getForecastsForToday(locationId);
        LocalTime now = LocalTime.now();

        List<Forecast> futureForecasts = new ArrayList<>();
        for (Forecast forecast : forecastsToday) {
            if (forecast.getTime().compareTo(now) >= 0) {
                futureForecasts.add(forecast);
            }
        }
        Forecast nextForecast = futureForecasts.get(0);

        int highestRainChance = 0;
        int avgRainChance = 0;
        LocalTime highestRainChanceTime = nextForecast.getTime();
        for (Forecast forecast : futureForecasts) {
            int chanceOfRain = Integer.parseInt(forecast.getPrecipitationProbability());
            avgRainChance += chanceOfRain;
            highestRainChance = Math.max(chanceOfRain, highestRainChance);
            highestRainChanceTime = (chanceOfRain > highestRainChance) ? forecast.getTime() : highestRainChanceTime;
        }
        avgRainChance = Math.round((float) avgRainChance / (float) forecastsPerDay);

        nextForecast.setAvgRainChanceNextDay(avgRainChance);
        nextForecast.setHighestRainChanceNextDay(highestRainChance);
        nextForecast.setHighestRainChanceTime(highestRainChanceTime);

        return nextForecast;
    }

    private Forecast getForecast(String locationId, int index) {
        String forecastUrl = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/" +
                locationId + "?res=3hourly&key=" + apiKey;
        JsonNode forecastJson = getJsonFromUrl(forecastUrl);

        String location = forecastJson.get("SiteRep").get("DV").get("Location").get("name").asText();
        JsonNode repNode = forecastJson.get("SiteRep").get("DV").get("Location").get("Period").get(0).get("Rep").get(index);

        forecastsPerDay = forecastJson.get("SiteRep").get("DV").get("Location").get("Period").get(0).get("Rep").size();
        return new Forecast(repNode, location);
    }

    private List<Forecast> getForecastsForToday(String locationId) {
        List<Forecast> nextDayForecasts = new ArrayList<>();
        for (int i = 0; i < forecastsPerDay; i++)
            nextDayForecasts.add(getForecast(locationId, i));

        return nextDayForecasts;
    }

    public void saveAllOverlays() {
        JsonNode layersCapabilities = getJsonFromUrl("http://datapoint.metoffice.gov.uk/public/data/layer/wxfcs/all/json/capabilities?key="
                + apiKey);
        for (JsonNode layer : layersCapabilities.get("Layers").get("Layer")) {
            String layerName = layer.get("Service").get("LayerName").asText();
            String time = layer.get("Service").get("Timesteps").get("@defaultTime").asText();
            String timestep = layer.get("Service").get("Timesteps").get("Timestep").get(0).asText();
            try {
                URL url = new URL("http://datapoint.metoffice.gov.uk/public/data/layer/wxfcs/" + layerName +
                        "/png?RUN=" + time + "Z&FORECAST=" + timestep + "&key=" + apiKey);
                Image image = ImageIO.read(url);
                File outputFile = new File("src/main/resources/static/images/" + layerName + ".png");
                ImageIO.write((RenderedImage) image, "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
