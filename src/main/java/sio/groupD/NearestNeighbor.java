/**
 * Authors : Jeremiah Steiner & Simon Guggisberg
 */

package sio.groupD;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

/**
 * Classe NearestNeighbor permet de calculer un tour à l'aide de l'heuristique du plus proche voisin
 */
public final class NearestNeighbor implements TspConstructiveHeuristic {

  private boolean[] citiesVisited;
  private int[] orderVisited;
  private int countVisited = 0;
  private int distTot = 0;

  /**
   * Calcule un tour en utilisant l'heuristique du NearestNeighbor
   * @param data Data of problem instance
   * @param startCityIndex Index of starting city, if needed by the implementation
   *
   * @return un TspTour, stockant les infos relatives au tour trouvé
   */
  @Override
  public TspTour computeTour(TspData data, int startCityIndex) {
    Init(data.getNumberOfCities(), startCityIndex);

    int currentClosest = Utils.getClosestCity(citiesVisited, data, startCityIndex);
    distTot += data.getDistance(currentClosest, startCityIndex);
    citiesVisited[currentClosest] = true;
    orderVisited[countVisited] = currentClosest;
    distTot += data.getDistance(startCityIndex, currentClosest);

    while (++countVisited < data.getNumberOfCities())
    {
      int previous = currentClosest;
      currentClosest = Utils.getClosestCity(citiesVisited, data, currentClosest);
      distTot += data.getDistance(currentClosest, previous);
      citiesVisited[currentClosest] = true;
      orderVisited[countVisited] = currentClosest;
    }

    return new TspTour(data, orderVisited, distTot);
  }

  /**
   * Initialise les variables nécessaires au calcul du tour
   * @param numberOfCities nombre total de villes lors de la simulation
   * @param startCityIndex index de la ville de départ
   */
  private void Init(int numberOfCities, int startCityIndex)
  {
    citiesVisited = new boolean[numberOfCities];
    orderVisited = new int[numberOfCities];
    countVisited = 0;
    distTot = 0;

    // réinitialisation, voir si nécessaire (selon fonctionnement du main)
    for (int i = 0; i < citiesVisited.length; ++i)
    {
      citiesVisited[i] = false;
    }
    for (int i = 0; i < orderVisited.length; ++i)
    {
      orderVisited[i] = -1;
    }
    citiesVisited[startCityIndex] = true;
    orderVisited[countVisited++] = startCityIndex;
  }
}
