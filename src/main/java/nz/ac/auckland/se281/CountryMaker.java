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

  public CountryMaker(List<String> countryStats, List<String> adjacencies) {
    this.countryStats = countryStats;
    this.adjacencies = adjacencies;
  }

  public List<String> getCountryNames() {
    for (String country : this.countryStats) {
      this.countryNames.add(country.split(",")[0].trim());
    }
    return this.countryNames;
  }

  public List<String> getContinent() {
    for (String country : this.countryStats) {
      String[] parts = country.split(",");
      this.continents.add(parts[1].trim());
    }
    return this.continents;
  }

  public List<String> getFuel() {
    for (String country : this.countryStats) {
      String[] parts = country.split(",");
      this.fuels.add(parts[2].trim());
    }
    return this.fuels;
  }

  public List<List<String>> getAdjacenciesWithoutSelf() {
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
