package training.metofficeweather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Locations {

    @JsonProperty(value="Location")
    private Location[] locations;

    public Location[] getLocations(){
        return locations;
    }

    public List<Location> searchLocationsByName(String name){
        List<Location> matchedLocations = new ArrayList<>();
        for(Location location: locations){
            if(location.getName().startsWith(name)) matchedLocations.add(location);
        }
        return matchedLocations;
    }

}
