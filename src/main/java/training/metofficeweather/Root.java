package training.metofficeweather;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Root {

    @JsonProperty(value = "Locations")
    private Locations locations;

    public Locations getLocations() {
        return locations;
    }
}
