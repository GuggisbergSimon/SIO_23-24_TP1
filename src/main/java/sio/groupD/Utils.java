/**
 * Authors : Jeremiah Steiner & Simon Guggisberg
 */

package sio.groupD;

import sio.tsp.TspData;

/**
 * Classe Utilitaire pour méthodes statiques partagées par plusieurs classes
 */
public class Utils {

    /**
     * Trouve l'indice de la ville la plus proche de la ville passée en paramètre
     * @param citiesVisited Un tableau de booléens représentant les villes déjà visitées, ou non
     * @param data données à fournir pour le calcul, contient notamment la distance entre chaque ville
     * @param city indice de la ville depuis laquelle on recherche la ville la plus proche
     * @return l'indice de la ville la plus proche ainsi que la distance jusqu'à celle-ci, si aucune ville disponible : -1
     */
    public static int getClosestCity(boolean[] citiesVisited, TspData data, int city)
    {
        int closestOne = -1;
        int distMin = Integer.MAX_VALUE;
        for (int i = 0; i < citiesVisited.length; ++i)
        {
            int distance = data.getDistance(i, city);
            if (!citiesVisited[i] && distMin > distance)
            {
                closestOne = i;
                distMin = distance;
            }
        }

        return closestOne;
    }
}
