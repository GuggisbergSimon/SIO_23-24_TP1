/**
 * Authors : Jeremiah Steiner & Simon Guggisberg
 */

package sio.groupD;

import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

import java.util.Arrays;

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
     *
     * @param data           Data of problem instance
     * @param startCityIndex Index of starting city, if needed by the implementation
     * @return un TspTour, stockant les infos relatives au tour trouvé
     */
    @Override
    public TspTour computeTour(TspData data, int startCityIndex) {
        Init(data.getNumberOfCities(), startCityIndex);

        int currentClosest = getClosestCityAndAddDistance(data, startCityIndex);
        citiesVisited[currentClosest] = true;
        orderVisited[countVisited] = currentClosest;

        while (++countVisited < data.getNumberOfCities()) {
            currentClosest = getClosestCityAndAddDistance(data, currentClosest);
            citiesVisited[currentClosest] = true;
            orderVisited[countVisited] = currentClosest;
        }

        return new TspTour(data, orderVisited, distTot);
    }

    /**
     * Initialise les variables nécessaires au calcul du tour
     *
     * @param numberOfCities nombre total de villes lors de la simulation
     * @param startCityIndex index de la ville de départ
     */
    private void Init(int numberOfCities, int startCityIndex) {
        citiesVisited = new boolean[numberOfCities];
        orderVisited = new int[numberOfCities];
        countVisited = 0;
        distTot = 0;

        Arrays.fill(citiesVisited, false);

        citiesVisited[startCityIndex] = true;
        orderVisited[countVisited++] = startCityIndex;
    }

    /**
     * Trouve l'indice de la ville la plus proche de la ville passée en paramètre, ajoute la distance jusqu'à celle-ci
     *
     * @param data données à fournir pour le calcul, contient notamment la distance entre chaque ville
     * @param city indice de la ville depuis laquelle on recherche la ville la plus proche
     * @return l'indice de la ville la plus proche, si aucune ville disponible : -1
     */
    private int getClosestCityAndAddDistance(TspData data, int city) {
        int closestOne = -1;
        int distMin = Integer.MAX_VALUE;

        for (int i = 0; i < citiesVisited.length; ++i) {
            if (!citiesVisited[i] && distMin > data.getDistance(i, city)) {
                closestOne = i;
                distMin = data.getDistance(i, city);
            }
        }
        // On retourne -1 s'il ne reste plus aucune ville à parcourir (ne devrait jamais arriver)
        if (closestOne == -1) return -1;
        // Sinon on ajoute la distance à la distance totale et on return l'indice de la ville trouvée
        distTot += distMin;
        return closestOne;
    }
}
