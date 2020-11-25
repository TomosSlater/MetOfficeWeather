package training.metofficeweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    private String elevation;
    private String id;
    private String latitude;
    private String longitude;
    private String name;
    private String region;
    private String unitaryAuthArea;
}
