package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// HashSet or LinkedHashSet (Set<__> var = new HashSet<>() OR new LinkedHashSet<>();) DO IT FOR
// CONTINENTS TRAVELLED
// LinkedList with the Queue (Queue<__> var = new LinkedList<>();) DONE
// LinkedList/ArrayList with the List DONE
// Throw and catch custom exception DONE
// At least two new classes. DONE

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

    this.countryStats = Utils.readCountries();
    this.adjacencies = Utils.readAdjacencies();
    CountryMaker country = new CountryMaker(this.countryStats, this.adjacencies);
    this.countryNames = country.getCountryNames();
    this.continents = country.getContinent();
    this.fuels = country.getFuel();
    this.adjacenciesWithoutSelf = country.getAdjacenciesWithoutSelf();
  }

  public void checkInput(String inputCountry) throws InvalidCountryException {
    if (!countryNames.contains(inputCountry)) {
      throw new InvalidCountryException();
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    loadMap();
    this.isInvalidCountry = true;

    while (this.isInvalidCountry) {
      MessageCli.INSERT_COUNTRY.printMessage();
      String inputCountry = Utils.scanner.nextLine().trim();
      inputCountry = Utils.capitalizeFirstLetterOfEachWord(inputCountry);
      try {
        checkInput(inputCountry);
        this.isInvalidCountry = false;
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
    while (isInvalidStartingCountry) {
      MessageCli.INSERT_SOURCE.printMessage();
      this.startingCountry = Utils.scanner.nextLine().trim();
      this.startingCountry = Utils.capitalizeFirstLetterOfEachWord(this.startingCountry);
      try {
        checkInput(this.startingCountry);
        this.isInvalidStartingCountry = false;
        System.out.println("You selected: " + startingCountry);
        while (isInvalidEndingCountry) {
          MessageCli.INSERT_DESTINATION.printMessage();
          this.endingCountry = Utils.scanner.nextLine().trim();
          this.endingCountry = Utils.capitalizeFirstLetterOfEachWord(this.endingCountry);
          try {
            checkInput(this.endingCountry);
            this.isInvalidEndingCountry = false;
            System.out.println("You selected: " + endingCountry);
          } catch (InvalidCountryException e) {
            MessageCli.INVALID_COUNTRY.printMessage(this.endingCountry);
          }
        }
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(this.startingCountry);
      }
    }
    // Checks if same starting and ending country
    if (this.startingCountry.equals(this.endingCountry)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }
    // Checks if direct neighbours
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

    Graph graph = new Graph(countryNames, adjacenciesWithoutSelf);
    List<String> totalRoute = graph.breathFirstTraversal(startingCountry, endingCountry);
    MessageCli.ROUTE_INFO.printMessage(totalRoute.toString());
    int fuelCost = 0;
    for (int i = 1; i < totalRoute.size() - 1; i++) {
      int index = countryNames.indexOf(totalRoute.get(i));
      fuelCost += Integer.valueOf(this.fuels.get(index));
    }
    MessageCli.FUEL_INFO.printMessage(fuelCost + "");

    Set<String> continentsTravelled = new HashSet<>();
    for (int i = 0; i < totalRoute.size(); i++) {
      int index = countryNames.indexOf(totalRoute.get(i));
      continentsTravelled.add(this.continents.get(index));
    }
    System.out.print(continentsTravelled);
  }
}
