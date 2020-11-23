package training.metofficeweather;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class ConsoleApplication {

    Locations locations;
    Scanner scan;

    public ConsoleApplication(Locations locations){
        this.locations = locations;
        scan = new Scanner(System.in);
    }

    public void runApplication(){
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
        if (command.equals("Quit")) System.exit(0);
        else if(command.equals("Help")) printHelp();
        else if(command.equals("List")) listLocations();
        else if (command.startsWith("List ")) listLocationsStartingWith(command);
        getUserInput();
    }

    private void listLocations(){
        for(Location location: locations.getLocations()) System.out.println(location.getName());
    }

    private void listLocationsStartingWith(String command){
        String searchTerm = "";
        String[] parts = command.split(" ");
        for(int i = 1; i < parts.length; i++){
            if (i > 1) searchTerm += " ";
            searchTerm += parts[i];
        }

        List<Location> matchedLocations = locations.searchLocationsByName(searchTerm);
        if(matchedLocations.isEmpty()) System.out.println("No locations match input '" + searchTerm + "'");
        else {
            for(Location location: matchedLocations) System.out.println(location.getName());
        }
    }

    private void printHelp(){
        System.out.println("Supported Commands: ");
        System.out.println("Help - lists all supported commands, but you already knew this");
        System.out.println("List - lists all location names");
        System.out.println("List [Word(s)] - lists all location names starting with the given words");
        System.out.println("Quit - quits the application");
    }
}
