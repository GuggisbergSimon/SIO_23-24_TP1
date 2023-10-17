package sio.groupD;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspTour;

public final class Main {

    private record TspMetaData (String name, int optimalLength) { }
    private record TspObservation(/*TspMetaData metaData, */TspConstructiveHeuristic algorithm, TspTour tour, long runTime) { }

    // TODO remove traces of MAX_VALUE_TEST for rendu
    private static final int MAX_VALUE_TEST = 1000;

    public static void main(String[] args) {
        // TODO
        //  - Implémentation des classes NearestNeighbor et DoubleEndsNearestNeighbor ;
        //  - Affichage des statistiques dans la classe Main ;
        //  - Documentation abondante des classes comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        //todo convert both names and optimalLengths table
        //TspMetaData[] metaDatas = {new TspMetaData("att532", 2)};

        String[] names = {"att532", "rat575", "rl1889", "u574", "u1817", "vm1748"};
        int[] optimalLengths = {86729, 6773, 316536, 36905, 57201, 336556};
        for (int i = 0; i < names.length; i++) {
            TspData data = null;
            try {
                data = TspData.fromFile("data/" + names[i] + ".dat");
            } catch (Exception e) {
                System.out.println("Something wrong happened while trying to read data from file : " + e);
                return;
            }

            TspConstructiveHeuristic[] algorithms = {new NearestNeighbor() /*, new DoubleEndsNearestNeighbor() */};
            for (TspConstructiveHeuristic algorithm : algorithms) {
                int nbrTours = data.getNumberOfCities();

                // TODO remove traces of MAX_VALUE_TEST for rendu
                if (nbrTours > MAX_VALUE_TEST) {
                    continue;
                }

                TspObservation[] observations = new TspObservation[nbrTours];
                for (int j = 0; j < nbrTours; j++) {
                    long start = System.nanoTime();
                    TspTour tour = algorithm.computeTour(data, j);
                    long end = System.nanoTime();
                    // (end - start) / 1000000 + "ms"
                    observations[j] = new TspObservation(algorithm, tour, end - start);
                }

                TspObservation minTour = observations[0];
                for (TspObservation observation : observations) {
                    if (observation.tour.length() < minTour.tour.length()) {
                        minTour = observation;
                    }
                }

                System.out.printf("file:%s algo:%s length:%d ratio:%f%n",
                        names[i],
                        algorithm.getClass().getName(),
                        minTour.tour.length(),
                        //  = 2
                        ((float) minTour.tour.length() / optimalLengths[i])
                );
            }
        }
    }
}