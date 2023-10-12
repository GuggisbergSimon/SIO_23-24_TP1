package sio.groupD;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

public final class NearestNeighbor implements TspConstructiveHeuristic {

  private boolean[] citiesVisited;
  private int[] orderVisited;
  private int countVisited = 0;
  private int distTot = 0;

  @Override
  public TspTour computeTour(TspData data, int startCityIndex) {
    citiesVisited = new boolean[data.getNumberOfCities()];
    orderVisited = new int[data.getNumberOfCities()];
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

    int currentClosest = getClosestCity(data, startCityIndex);
    citiesVisited[currentClosest] = true;
    orderVisited[countVisited++] = currentClosest;
    distTot += data.getDistance(startCityIndex, currentClosest);

    while (countVisited < data.getNumberOfCities())
    {
      currentClosest = getClosestCity(data, startCityIndex);
      citiesVisited[currentClosest] = true;
      orderVisited[countVisited++] = currentClosest;
    }

    return new TspTour(data, orderVisited, distTot);
  }

  private int getClosestCity(TspData data, int city)
  {
    int closestOne = -1;
    int distMin = Integer.MAX_VALUE;

    for (int i = 0; i < citiesVisited.length; ++i)
    {
      if (!citiesVisited[i] && distMin > data.getDistance(i, city))
      {
        closestOne = i;
        distMin = data.getDistance(i, city);

      }
    }
    distTot += data.getDistance(closestOne, city);
    return closestOne;
  }
}
