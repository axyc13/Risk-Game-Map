package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

/** This class is the main entry point. */
public class MapEngine {

  private List<String> countryStats;
  private List<String> adjacencies;
  private List<String> countryNames = new ArrayList<>();
  private List<String> continents = new ArrayList<>();
  private List<String> fuels = new ArrayList<>();
  private List<List<String>> adjacenciesWithoutSelf = new ArrayList<>();
  private boolean isInvalidCountry = true;
  private boolean isInvalidStartingCountry = true;
  private boolean isInvalidEndingCountry = true;
  private String startingCountry;
  private String endingCountry;

  public MapEngine() {
    loadMap();
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {
    // Initialise variables
    this.countryStats = Utils.readCountries();
    this.adjacencies = Utils.readAdjacencies();
    CountryMaker country = new CountryMaker(this.countryStats, this.adjacencies);
    this.countryNames = country.getCountryNames();
    this.continents = country.getContinent();
    this.fuels = country.getFuel();
    this.adjacenciesWithoutSelf = country.getAdjacenciesWithoutSelf();
  }

  /** this method is invoked when checking for an exception */
  public void checkInput(String inputCountry) throws InvalidCountryException {
    if (!countryNames.contains(inputCountry)) {
      throw new InvalidCountryException();
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    loadMap();
    this.isInvalidCountry = true;

    // Loop until valid user input
    while (this.isInvalidCountry) {
      MessageCli.INSERT_COUNTRY.printMessage();
      String inputCountry = Utils.scanner.nextLine().trim();
      inputCountry = Utils.capitalizeFirstLetterOfEachWord(inputCountry);
      try {
        checkInput(inputCountry);

        this.isInvalidCountry = false;
        // Print country information
        int index = countryNames.indexOf(inputCountry);
        MessageCli.COUNTRY_INFO.printMessage(
            inputCountry,
            this.continents.get(index),
            this.fuels.get(index),
            this.adjacenciesWithoutSelf.get(index).toString());
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(inputCountry);
      }
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {
    loadMap();
    this.isInvalidStartingCountry = true;
    this.isInvalidEndingCountry = true;

    // Loop until valid user input for starting and ending country
    while (isInvalidStartingCountry) {
      MessageCli.INSERT_SOURCE.printMessage();
      this.startingCountry = Utils.scanner.nextLine().trim();
      this.startingCountry = Utils.capitalizeFirstLetterOfEachWord(this.startingCountry);
      try {
        checkInput(this.startingCountry);

        this.isInvalidStartingCountry = false;
        while (isInvalidEndingCountry) {
          MessageCli.INSERT_DESTINATION.printMessage();
          this.endingCountry = Utils.scanner.nextLine().trim();
          this.endingCountry = Utils.capitalizeFirstLetterOfEachWord(this.endingCountry);
          try {
            checkInput(this.endingCountry);

            this.isInvalidEndingCountry = false;
          } catch (InvalidCountryException e) {
            MessageCli.INVALID_COUNTRY.printMessage(this.endingCountry);
          }
        }
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(this.startingCountry);
      }
    }

    // Checks if we have the same starting and ending country
    if (this.startingCountry.equals(this.endingCountry)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // Checks if they are direct neighbours
    int indexofStarting = countryNames.indexOf(startingCountry);
    int indexofEnding = countryNames.indexOf(endingCountry);
    if (this.adjacenciesWithoutSelf.get(indexofStarting).contains(endingCountry)) {
      MessageCli.ROUTE_INFO.printMessage("[" + startingCountry + ", " + endingCountry + "]");
      MessageCli.FUEL_INFO.printMessage("0");
      MessageCli.CONTINENT_INFO.printMessage(
          "["
              + this.continents.get(indexofStarting)
              + " (0), "
              + this.continents.get(indexofEnding)
              + " (0)]");
      return;
    }

    // If not, we create a graph and find the optimal route
    Graph graph = new Graph(countryNames, adjacenciesWithoutSelf);

    List<String> totalRoute = graph.breathFirstTraversal(startingCountry, endingCountry);
    MessageCli.ROUTE_INFO.printMessage(totalRoute.toString());

    // Find total fuel cost
    int fuelCost = 0;
    for (int i = 1; i < totalRoute.size() - 1; i++) {
      int index = countryNames.indexOf(totalRoute.get(i));
      fuelCost += Integer.valueOf(this.fuels.get(index));
    }
    MessageCli.FUEL_INFO.printMessage(fuelCost + "");

    // Find continents travelled
    List<String> continentsTravelled = new ArrayList<>();
    for (int i = 0; i < totalRoute.size(); i++) {
      int index = countryNames.indexOf(totalRoute.get(i));
      if (!continentsTravelled.contains(this.continents.get(index))) {
        continentsTravelled.add(this.continents.get(index));
      }
    }

    // Find fuel cost per continent
    List<Integer> continentFuelCount = new ArrayList<>();
    int fuelPerContinent = 0;
    for (String continent : continentsTravelled) {
      for (int i = 0; i < totalRoute.size(); i++) {
        int index = countryNames.indexOf(totalRoute.get(i));
        if (this.continents.get(index).equals(continent)) {
          fuelPerContinent += Integer.valueOf(this.fuels.get(index));
          if (i == 0 || i == totalRoute.size() - 1) {
            fuelPerContinent -= Integer.valueOf(this.fuels.get(index));
          }
        }
      }
      continentFuelCount.add(fuelPerContinent);
      fuelPerContinent = 0;
    }

    // Print continents travelled and fuel cost per continent
    String test = "[";
    for (int i = 0; i < continentsTravelled.size(); i++) {
      String continent = continentsTravelled.get(i);
      int fuel = continentFuelCount.get(i);
      test += continent + " (" + fuel + "), ";
    }
    test = test.substring(0, test.length() - 2) + "]";
    MessageCli.CONTINENT_INFO.printMessage(test);

    // Find continent with most fuel cost
    int mostFuel = continentFuelCount.get(0);
    for (int i = 0; i < continentFuelCount.size(); i++) {
      if (continentFuelCount.get(i) > mostFuel) {
        mostFuel = continentFuelCount.get(i);
      }
    }
    String continentWithMostFuel = continentsTravelled.get(continentFuelCount.indexOf(mostFuel));
    MessageCli.FUEL_CONTINENT_INFO.printMessage(continentWithMostFuel + " (" + mostFuel + ")");
  }
}
