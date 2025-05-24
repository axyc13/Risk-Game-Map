package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

public class CountryMaker {
  private List<String> countryStats;
  private List<String> adjacencies;
  private List<String> countryNames = new ArrayList<>();
  private List<String> adjacenciesWithoutSelf = new ArrayList<>();

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

  public List<String> getAdjacencies() {
    for (String adjacency : this.adjacencies) {
      String[] parts = adjacency.split(",");

      StringBuilder sb = new StringBuilder();
      for (int i = 1; i < parts.length; i++) {
        sb.append(parts[i].trim());
        if (i != parts.length - 1) {
          sb.append(", ");
        }
      }
      this.adjacenciesWithoutSelf.add(sb.toString());
    }
    return this.adjacenciesWithoutSelf;
  }
}
