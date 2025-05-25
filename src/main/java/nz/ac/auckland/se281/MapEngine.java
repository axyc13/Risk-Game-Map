package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

// HashSet or LinkedHashSet (Set<__> var = new HashSet<>() OR new LinkedHashSet<>();)
// LinkedList with the Queue (Queue<__> var = new LinkedList<>();)
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
    } else {
      isInvalidCountry = false;
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    loadMap();
    isInvalidCountry = true;

    while (isInvalidCountry) {
      MessageCli.INSERT_COUNTRY.printMessage();
      String inputCountry = Utils.scanner.nextLine().trim();
      inputCountry = Utils.capitalizeFirstLetterOfEachWord(inputCountry);
      try {
        checkInput(inputCountry);
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
    isInvalidCountry = true;
    while (isInvalidCountry) {
      MessageCli.INSERT_SOURCE.printMessage();
      this.startingCountry = Utils.scanner.nextLine().trim();
      this.startingCountry = Utils.capitalizeFirstLetterOfEachWord(startingCountry);
      try {
        checkInput(startingCountry);
        System.out.println("You selected: " + startingCountry);
        MessageCli.INSERT_DESTINATION.printMessage();
        this.endingCountry = Utils.scanner.nextLine().trim();
        this.endingCountry = Utils.capitalizeFirstLetterOfEachWord(endingCountry);
        try {
          checkInput(endingCountry);
          System.out.println("You selected: " + endingCountry);
        } catch (InvalidCountryException e) {
          MessageCli.INVALID_COUNTRY.printMessage(endingCountry);
        }
      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(startingCountry);
      }
    }
    if (startingCountry.equals(endingCountry)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }
  }
}
