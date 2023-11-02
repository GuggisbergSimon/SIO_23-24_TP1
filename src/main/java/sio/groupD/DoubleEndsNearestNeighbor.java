/**
 * Authors : Jeremiah Steiner & Simon Guggisberg
 */

package sio.groupD;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class DoubleEndsNearestNeighbor permet de calculer un tour à l'aide de l'heuristique des plus proches voisins par les deux bouts
 */
public final class DoubleEndsNearestNeighbor implements TspConstructiveHeuristic {

  private int numberOfCities;
  private boolean[] citiesVisited;
  private int firstCounter = 0;
  private int[] visited;
  private int lastCounter = 0;
  private int countVisited = 0;
  private int distTot = 0;


  /**
   * Calcule un tour en utilisant l'heuristique du DoubleEndsNearestNeighbor
   * @param data Data of problem instance
   * @param startCityIndex Index of starting city, if needed by the implementation
   *
   * @return un TspTour, stockant les infos relatives au tour trouvé
   */
  @Override
  public TspTour computeTour(TspData data, int startCityIndex) {
    Init(data.getNumberOfCities(), startCityIndex);

    while (countVisited++ < numberOfCities)
    {
      getClosestCityAndAddIt(data);
    }

    return new TspTour(data, visited, distTot);
  }

  /**
   * Initialise les variables nécessaires au calcul du tour
   * @param numberOfCities nombre total de villes lors de la simulation
   * @param startCityIndex index de la ville de départ
   */
  private void Init(int numberOfCities, int startCityIndex)
  {
    this.numberOfCities = numberOfCities;
    citiesVisited = new boolean[numberOfCities];
    firstCounter = 1;
    lastCounter = 0;
    countVisited = 0;
    visited = new int[numberOfCities];
    visited[countVisited++] = startCityIndex;
    distTot = 0;

    citiesVisited[startCityIndex] = true;
  }

  /**
   * Met à jour le tour en l'ajoutant au bon endroit de la liste (à l'avant ou à l'arrière suivant la distance)
   * @param data données à fournir pour le calcul, contient notamment la distance entre chaque ville
   */
  private void getClosestCityAndAddIt(TspData data)
  {
    int closestOne = -1;
    int distMin = Integer.MAX_VALUE;
    boolean byFirst = false;
    int indiceToModify = -1;

    for (int i = 0; i < numberOfCities; ++i)
    {
      if (citiesVisited[i]) continue;
      if (countVisited == 2) // premier tour
      {
        if (distMin > data.getDistance(i, visited[0]))
        {
          closestOne = i;
          distMin = data.getDistance(i, visited[0]);
          indiceToModify = numberOfCities - 1;
        }
        continue;
      }
      if (distMin > data.getDistance(i, visited[firstCounter]))
      {
        closestOne = i;
        indiceToModify = firstCounter + 1;
        distMin = data.getDistance(i, visited[firstCounter]);
        byFirst = true;
      }
      // si dist ==, cela ne rentre pas dans le second if
      if (distMin > data.getDistance(i, visited[numberOfCities - lastCounter]))
      {
        closestOne = i;
        indiceToModify = numberOfCities - lastCounter - 1;
        distMin = data.getDistance(i, visited[numberOfCities - lastCounter]);
        byFirst = false;
      }
    }
    if (closestOne == -1) return;

    distTot += distMin;
    citiesVisited[closestOne] = true;
    visited[indiceToModify] = closestOne;

    if (byFirst)
    {
      ++firstCounter;
    }
    else
    {
      ++lastCounter;
    }
  }
}
