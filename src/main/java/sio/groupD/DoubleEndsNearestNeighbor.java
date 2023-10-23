package sio.groupD;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

import java.util.LinkedList;
import java.util.List;

public final class DoubleEndsNearestNeighbor implements TspConstructiveHeuristic {

  private boolean[] citiesVisited;
  private List<Integer> orderVisited = new LinkedList<>();
  private int countVisited = 0;
  private int distTot = 0;
  private int currentCityStart = -1;
  private int currentCityEnd = -1;


  @Override
  public TspTour computeTour(TspData data, int startCityIndex) {
    Init(data.getNumberOfCities(), startCityIndex);

    currentCityEnd = getClosestCity(data, startCityIndex);
    citiesVisited[currentCityEnd] = true;
    orderVisited.add(currentCityEnd);
    distTot += data.getDistance(startCityIndex, currentCityEnd);

    while (++countVisited < data.getNumberOfCities())
    {
      getClosestCityAndAddIt(data);
    }

    return new TspTour(data, orderVisited.stream().mapToInt(i->i).toArray(), distTot);
  }

  private void Init(int numberOfCities, int startCityIndex)
  {
    currentCityStart = startCityIndex;
    currentCityEnd = -1;
    citiesVisited = new boolean[numberOfCities];
    orderVisited = new LinkedList<>();
    countVisited = 1;
    distTot = 0;

    // réinitialisation, voir si nécessaire (selon fonctionnement du main)
    for (int i = 0; i < citiesVisited.length; ++i)
    {
      citiesVisited[i] = false;
    }

    citiesVisited[startCityIndex] = true;
    orderVisited.add(startCityIndex);
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

  private void getClosestCityAndAddIt(TspData data)
  {
    int closestOne = -1;
    int distMin = Integer.MAX_VALUE;
    int bestCityFrom = -1;

    for (int i = 0; i < citiesVisited.length; ++i)
    {
      if (citiesVisited[i]) continue;
      if (distMin > data.getDistance(i, currentCityStart))
      {
        closestOne = i;
        distMin = data.getDistance(i, currentCityStart);
        bestCityFrom = currentCityStart;
      }
      // si dist =, cela ne rentre pas dans le 2ème if
      if (distMin > data.getDistance(i, currentCityEnd))
      {
        closestOne = i;
        distMin = data.getDistance(i, currentCityEnd);
        bestCityFrom = currentCityEnd;
      }
    }

    distTot += data.getDistance(closestOne, bestCityFrom);
    citiesVisited[closestOne] = true;

    if (currentCityStart == bestCityFrom)
    {
      orderVisited.add(0, closestOne);
      currentCityStart = closestOne;
    }
    else
    {
      orderVisited.add(orderVisited.size(), closestOne);
      currentCityEnd = closestOne;
    }
  }
}
