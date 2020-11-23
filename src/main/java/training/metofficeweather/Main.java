package training.metofficeweather;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {
    public static void main(String[] args) {

        try {
            ConsoleApplication console = new ConsoleApplication();
            console.runApplication();

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}	
