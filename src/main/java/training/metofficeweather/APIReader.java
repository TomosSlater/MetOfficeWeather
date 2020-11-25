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
import java.util.ArrayList;
import java.util.List;

public class APIReader {

    @JsonProperty(value = "Locations")
    private final Client client = ClientBuilder.newClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey = System.getenv("MET_KEY");
    private int forecastsPerDay = 1;

    public Locations getLocations() {
        String inputData = client.target("http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key=" + apiKey)
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
        Forecast forecast = getForecast(locationId, 0);


        int highestRainChance = 0;
        int avgRainChance = 0;
        List<Integer> forecastsForNextDay = getPrecipitaionChanceForNextDay(locationId);
        for(Integer chance: forecastsForNextDay){
            avgRainChance += chance;
            highestRainChance = (chance > highestRainChance) ? chance: highestRainChance;
        }
        avgRainChance = Math.round((float)avgRainChance / (float)forecastsPerDay);

        forecast.setAvgRainChanceNextDay(avgRainChance);
        forecast.setHighestRainChanceNextDay(highestRainChance);

        return forecast;
    }

    private Forecast getForecast(String locationId, int index){
        String forecastUrl = createLocationUrl(locationId);
        JsonNode forecastJson = getJsonFromUrl(forecastUrl);

        String location = forecastJson.get("SiteRep").get("DV").get("Location").get("name").asText();
        JsonNode repNode = forecastJson.get("SiteRep").get("DV").get("Location").get("Period").get(0).get("Rep").get(index);

        forecastsPerDay = forecastJson.get("SiteRep").get("DV").get("Location").get("Period").get(0).get("Rep").size();
        return new Forecast(repNode, location);
    }


    private List<Integer> getPrecipitaionChanceForNextDay(String locationId){
        List<Integer> precipitationChances = new ArrayList<>();
        for(int i = 0; i < forecastsPerDay; i++)
            precipitationChances.add(Integer.parseInt(getForecast(locationId, i).getPrecipitationProbability()));

        return precipitationChances;
    }


    private String createLocationUrl(String locationId) {
        return "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/" +
                locationId + "?res=3hourly&key=" + apiKey;
    }

    public String getRainfallOverlayUrl() {
        JsonNode layersCapabilities = getJsonFromUrl("http://datapoint.metoffice.gov.uk/public/data/layer/wxfcs/all/json/capabilities?key=" + apiKey);
        JsonNode timestepsNode = layersCapabilities.get("Layers").get("Layer").get(0).get("Service").get("Timesteps");
        String timestep = timestepsNode.get("Timestep").get(0).asText();
        String time = timestepsNode.get("@defaultTime").asText();
        return "http://datapoint.metoffice.gov.uk/public/data/layer/wxfcs/Precipitation_Rate/png?RUN=" + time + "Z&FORECAST=" + timestep + "&key=" + apiKey;
    }

    public void saveForecastImage() {
        try {
            String urlString = getRainfallOverlayUrl();
            System.out.println(urlString);
            URL url = new URL(urlString);
            Image image = ImageIO.read(url);
            File outputfile = new File("src/main/resources/static/images/overlay.png");
            ImageIO.write((RenderedImage) image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
