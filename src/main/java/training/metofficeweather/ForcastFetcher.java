package training.metofficeweather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class ForcastFetcher {
    private final String locationId;
    private final Client client = ClientBuilder.newClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ForcastFetcher(String locationId){
        this.locationId = locationId;
    }

    private String getNextForcastTime(){
        JsonNode nextForcastJson = getJsonFromUrl("http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/capabilities?res=3hourly&key=0224acb8-8d90-4c16-923e-53020daded52");
        return nextForcastJson.get("Resource").get("TimeSteps").get("TS").get(0).asText();
    }

    private JsonNode getJsonFromUrl(String url){
        String json = client.target(url)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createLocationUrl(String time){
        return "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/" +
                locationId + "?time=" + time + "&res=3hourly&key=0224acb8-8d90-4c16-923e-53020daded52";
    }

    public Forcast getForcast(){
        String time = getNextForcastTime();
        String forcastUrl = createLocationUrl(time);
        JsonNode forcastJson = getJsonFromUrl(forcastUrl);

        String location = forcastJson.get("SiteRep").get("DV").get("Location").get("name").asText();
        JsonNode repNode = forcastJson.get("SiteRep").get("DV").get("Location").get("Period").get("Rep");

        return new Forcast(repNode, location);

    }


}
