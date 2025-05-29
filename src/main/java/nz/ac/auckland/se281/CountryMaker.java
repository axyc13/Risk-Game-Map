package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

public class CountryMaker {
  private List<String> countryStats;
  private List<String> adjacencies;
  private List<String> countryNames = new ArrayList<>();
  private List<List<String>> adjacenciesWithoutSelf = new ArrayList<>();
  private List<String> continents = new ArrayList<>();
  private List<String> fuels = new ArrayList<>();

  /**
   * Constructor for the CountryMaker class.
   *
   * @param countryStats list where each entry is a string formatted as "Country, Continent,
   *     FuelCost".
   * @param adjacencies list of adjacent countries formatted as "Country, Adjacent1, Adjacent2,
   *     etc".
   */
  public CountryMaker(List<String> countryStats, List<String> adjacencies) {
    this.countryStats = countryStats;
    this.adjacencies = adjacencies;
  }

  /**
   * This method extracts the country names from the countryStats list.
   *
   * @return List of country names.
   */
  public List<String> getCountryNames() {
    // Extract country names
    for (String country : this.countryStats) {
      this.countryNames.add(country.split(",")[0].trim());
    }
    return this.countryNames;
  }

  /**
   * This method extracts the continent of each country from the countryStats list.
   *
   * @return List of continents for each country.
   */
  public List<String> getContinent() {
    // Extract continents
    for (String country : this.countryStats) {
      String[] parts = country.split(",");
      this.continents.add(parts[1].trim());
    }
    return this.continents;
  }

  /**
   * This method extracts the fuel costs from the countryStats list.
   *
   * @return List of fuel costs for each country.
   */
  public List<String> getFuel() {
    // Extract fuel costs
    for (String country : this.countryStats) {
      String[] parts = country.split(",");
      this.fuels.add(parts[2].trim());
    }
    return this.fuels;
  }

  /**
   * This method extracts the adjacencies of each country from the adjacencies list minus itself.
   *
   * @return List of lists containing adjacent countries without self.
   */
  public List<List<String>> getAdjacenciesWithoutSelf() {
    // Extract the countries adjacencies without itself
    List<String> test = new ArrayList<>();
    for (String adjacency : this.adjacencies) {
      String[] parts = adjacency.split(",");

      for (int i = 1; i < parts.length; i++) {
        test.add(parts[i].trim());
      }
      this.adjacenciesWithoutSelf.add(test);
      test = new ArrayList<>();
    }
    return this.adjacenciesWithoutSelf;
  }
}
