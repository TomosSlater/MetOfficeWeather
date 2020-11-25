package training.metofficeweather.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import training.metofficeweather.APIReader;
import training.metofficeweather.Forecast;
import training.metofficeweather.Location;
import training.metofficeweather.Locations;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Controller
@EnableAutoConfiguration
public class Website {

    private final APIReader reader = new APIReader();

    @RequestMapping("/")
    ModelAndView home() throws JsonProcessingException {
        Locations inputData = reader.getLocations();
        List<String> locations = new ArrayList<>();
        for(Location location: inputData.getLocations()) locations.add(location.getName());
        Collections.sort(locations);

        return new ModelAndView("index", "locations", locations);
    }

    @RequestMapping("/weatherInfo")
    ModelAndView weatherInfo(@RequestParam("location") String location) {

        Locations locations = reader.getLocations();
        String id = locations.getIdOfLocation(location);
        Forecast forecast = reader.getForecast(id);

        return new ModelAndView("info", "forecast", forecast) ;
    }

    public static void main(String[] args) {
        SpringApplication.run(Website.class, args);
    }

}