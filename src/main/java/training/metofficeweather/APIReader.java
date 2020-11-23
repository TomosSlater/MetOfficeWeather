package training.metofficeweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class APIReader {

    @JsonProperty(value="Locations")
    private final Client client = ClientBuilder.newClient();
    private final ObjectMapper objectMapper = new ObjectMapper();;

    public Locations getLocations() throws JsonProcessingException {
        String inputData = client.target("http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key=42492468-7351-44a8-a25c-b3a7c4f10599")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        try {
            return objectMapper.readValue(inputData, Root.class).getLocations();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getNextForecastTime() {
        JsonNode nextForecastJson = getJsonFromUrl("http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/capabilities?res=3hourly&key=0224acb8-8d90-4c16-923e-53020daded52");
        return nextForecastJson.get("Resource").get("TimeSteps").get("TS").get(0).asText();
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

    private String createLocationUrl(String locationId, String time) {
        return "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/" +
                locationId + "?time=" + time + "&res=3hourly&key=0224acb8-8d90-4c16-923e-53020daded52";
    }

    public Forecast getForecast(String locationId) {
        String time = getNextForecastTime();
        String forecastUrl = createLocationUrl(locationId, time);
        JsonNode forecastJson = getJsonFromUrl(forecastUrl);

        String location = forecastJson.get("SiteRep").get("DV").get("Location").get("name").asText();
        JsonNode repNode = forecastJson.get("SiteRep").get("DV").get("Location").get("Period").get("Rep");

        return new Forecast(repNode, location);
    }

}
