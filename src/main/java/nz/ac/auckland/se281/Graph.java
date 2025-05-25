package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Graph {
  private Map<String, List<String>> adjNodes;

  public Graph(List<String> countries, List<List<String>> neighbors) {
    adjNodes = new HashMap<>();

    for (int i = 0; i < countries.size(); i++) {
      String country = countries.get(i);
      List<String> neighborList = neighbors.get(i);
      adjNodes.put(country, neighborList);
    }
  }

  public List<String> breathFirstTraversal(String root, String end) {
    List<String> visited = new ArrayList<>();
    Queue<String> queue = new LinkedList<>();
    queue.add(root);
    visited.add(root);
    while (!queue.isEmpty()) {
      String node = queue.poll();
      for (String n : adjNodes.get(node)) {
        if (!visited.contains(n)) {
          if (n.equals(end)) {
            visited.add(n);
            return visited;
          }
          visited.add(n);
          queue.add(n);
        }
      }
    }
    return visited;
  }
}
