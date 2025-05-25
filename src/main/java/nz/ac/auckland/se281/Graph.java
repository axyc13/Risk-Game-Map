package nz.ac.auckland.se281;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph {
  private Map<String, List<String>> adjNodes;

  public Graph(List<String> countries, List<List<String>> neighbors) {
    adjNodes = new HashMap<>();
    // Initialise Graph
    for (int i = 0; i < countries.size(); i++) {
      String country = countries.get(i);
      List<String> neighborList = neighbors.get(i);
      adjNodes.put(country, neighborList);
    }
  }

  public List<String> breathFirstTraversal(String root, String end) {
    // Perform BFS to find the optimal path from root to target end country
    Set<String> visited = new HashSet<>();
    Queue<String> queue = new LinkedList<>();
    Map<String, String> travelMap = new HashMap<>();
    List<String> optimalPath = new LinkedList<>();

    queue.add(root);
    visited.add(root);
    travelMap.put(root, null);

    while (!queue.isEmpty()) {
      String currentNode = queue.poll();

      // If we reach the target country, backtrack to find the optimal path
      if (currentNode.equals(end)) {
        while (currentNode != null) {
          optimalPath.add(0, currentNode);
          currentNode = travelMap.get(currentNode);
        }
        return optimalPath;
      }
      // Travel through adjacent nodes until we reach the target
      for (String n : adjNodes.get(currentNode)) {
        if (!visited.contains(n)) {
          visited.add(n);
          travelMap.put(n, currentNode);
          queue.add(n);
        }
      }
    }

    return optimalPath; // This will be empty if no path is found
  }
}
