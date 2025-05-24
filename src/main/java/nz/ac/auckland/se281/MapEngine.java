package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

// HashSet or LinkedHashSet (Set<__> var = new HashSet<>() OR new LinkedHashSet<>();)
// LinkedList with the Queue (Queue<__> var = new LinkedList<>();)
// LinkedList/ArrayList with the List DONE
// Throw and catch custom exception
// At least two new classes.

/** This class is the main entry point. */
public class MapEngine {

  private List<String> countryStats;
  private List<String> adjacencies;
  private List<String> countryNames = new ArrayList<>();
  private boolean isInvalidCountry = true;

  public MapEngine() {
    loadMap();
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {

    this.countryStats = Utils.readCountries();
    this.adjacencies = Utils.readAdjacencies();
    for (String country : this.countryStats) {
      countryNames.add(country.split(",")[0].trim());
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    MapEngine map = new MapEngine();
    while (isInvalidCountry) {
      MessageCli.INSERT_COUNTRY.printMessage();
      String inputCountry = Utils.scanner.nextLine().trim();
      if (countryNames.contains(Utils.capitalizeFirstLetterOfEachWord(inputCountry))) {
        isInvalidCountry = false;
      }
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}
