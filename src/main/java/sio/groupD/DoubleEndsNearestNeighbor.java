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
 * Classe DoubleEndsNearestNeighbor permet de calculer un tour à l'aide de l'heuristique des plus proches voisins par les deux bouts
 */
public final class DoubleEndsNearestNeighbor implements TspConstructiveHeuristic {

    private boolean[] citiesVisited;
    private List<Integer> orderVisited = new LinkedList<>();
    private int countVisited = 0;
    private int distTot = 0;
    private int currentCityStart = -1;
    private int currentCityEnd = -1;


    /**
     * Calcule un tour en utilisant l'heuristique du DoubleEndsNearestNeighbor
     *
     * @param data           Data of problem instance
     * @param startCityIndex Index of starting city, if needed by the implementation
     * @return un TspTour, stockant les infos relatives au tour trouvé
     */
    @Override
    public TspTour computeTour(TspData data, int startCityIndex) {
        Init(data.getNumberOfCities(), startCityIndex);

        currentCityEnd = currentCityStart;

        while (++countVisited < data.getNumberOfCities()) {
            getClosestCityAndAddIt(data);
        }

        return new TspTour(data, orderVisited.stream().mapToInt(i -> i).toArray(), distTot);
    }

    /**
     * Initialise les variables nécessaires au calcul du tour
     *
     * @param numberOfCities nombre total de villes lors de la simulation
     * @param startCityIndex index de la ville de départ
     */
    private void Init(int numberOfCities, int startCityIndex) {
        currentCityStart = startCityIndex;
        currentCityEnd = -1;
        citiesVisited = new boolean[numberOfCities];
        orderVisited = new LinkedList<>();
        countVisited = 1;
        distTot = 0;

        Arrays.fill(citiesVisited, false);

        citiesVisited[startCityIndex] = true;
        orderVisited.add(startCityIndex);
    }

    /**
     * Met à jour le tour en l'ajoutant au bon endroit de la liste (à l'avant ou à l'arrière suivant la distance)
     *
     * @param data données à fournir pour le calcul, contient notamment la distance entre chaque ville
     */
    private void getClosestCityAndAddIt(TspData data) {
        int closestOne = -1;
        int distMin = Integer.MAX_VALUE;
        int bestCityFrom = -1;

        for (int i = 0; i < citiesVisited.length; ++i) {
            if (citiesVisited[i]) continue;
            if (distMin > data.getDistance(i, currentCityStart)) {
                closestOne = i;
                distMin = data.getDistance(i, currentCityStart);
                bestCityFrom = currentCityStart;
            }
            // Si dist =, cela ne rentre pas dans le 2ème if
            if (distMin > data.getDistance(i, currentCityEnd)) {
                closestOne = i;
                distMin = data.getDistance(i, currentCityEnd);
                bestCityFrom = currentCityEnd;
            }
        }

        distTot += data.getDistance(closestOne, bestCityFrom);
        citiesVisited[closestOne] = true;

        if (currentCityStart == bestCityFrom) {
            orderVisited.add(0, closestOne);
            currentCityStart = closestOne;
        } else {
            orderVisited.add(orderVisited.size(), closestOne);
            currentCityEnd = closestOne;
        }
    }
}
