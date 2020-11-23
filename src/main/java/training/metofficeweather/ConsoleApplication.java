package training.metofficeweather;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Scanner;
import java.util.List;

public class ConsoleApplication {

    private Locations locations;
    private final Scanner scan = new Scanner(System.in);
    private final APIReader reader = new APIReader();

    public void runApplication() throws JsonProcessingException {
        this.locations = reader.getLocations();

        System.out.println("==========================");
        System.out.println("Welcome to our Weather app");
        System.out.println("==========================");
        System.out.println("Use command \"Help\" to get a list of commands");

        getUserInput();
    }

    private void getUserInput(){
        System.out.print("\nEnter command: >");
        String input = scan.nextLine();
        executeCommand(input);
    }

    private void executeCommand(String command){
        if (command.equalsIgnoreCase("Quit")) System.exit(0);
        else if(command.equalsIgnoreCase("Help")) printHelp();
        else if(command.equalsIgnoreCase("List")) listLocations();
        else if (command.toLowerCase().startsWith("list ")) listLocationsStartingWith(command);
        else if (command.toLowerCase().startsWith("forcast ")) getForcast(command);
        else System.out.println("Invalid command entered, use 'Help' to get a list of commands");
        getUserInput();
    }

    private void listLocations(){
        for(Location location: locations.getLocations()) System.out.println(location.getName());
    }

    private String stripCommand(String command){
        String newCommand = "";
        String[] parts = command.split(" ");
        for(int i = 1; i < parts.length; i++){
            if (i > 1) newCommand += " ";
            newCommand += parts[i];
        }
        return newCommand;
    }

    private void listLocationsStartingWith(String command){
        String searchTerm = stripCommand(command);

        List<Location> matchedLocations = locations.searchLocationsByName(searchTerm);
        if(matchedLocations.isEmpty()) System.out.println("No locations match input '" + searchTerm + "'");
        else {
            for(Location location: matchedLocations) System.out.println(location.getName());
        }
    }

    private void getForcast(String name){
        String searchTerm = stripCommand(name);

        String id = locations.getIdOfLocation(searchTerm);
        if(id.equals("")) System.out.println("Invalid location entered, please try again");
        else {
            Forcast forcast = reader.getForcast(id);
            forcast.printWeatherForcast();
        }
    }

    private void printHelp(){
        System.out.println("Supported Commands: ");
        System.out.println("Help - lists all supported commands, but you already knew this");
        System.out.println("List - lists all location names");
        System.out.println("List [Word(s)] - lists all location names starting with the given words");
        System.out.println("Forcast [Location] - gets the forcast for the given location");
        System.out.println("Quit - quits the application");
    }
}
