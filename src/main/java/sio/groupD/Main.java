package sio.groupD;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspTour;

public final class Main {

    private record TspMetaData (String name, int optimalLength) { }
    private record TspObservation(/*TspMetaData metaData, */TspConstructiveHeuristic algorithm, TspTour tour, long runTime) { }

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
        for (String name : names) {
            TspData data = null;
            try {
                data = TspData.fromFile("data/" + name + ".dat");
            } catch (Exception e) {
                System.out.println("Something wrong happened while trying to read data from file : " + e);
                return;
            }

            TspConstructiveHeuristic[] algorithms = {new NearestNeighbor(), new DoubleEndsNearestNeighbor()};
            for (TspConstructiveHeuristic algorithm : algorithms) {
                int nbrTours = data.getNumberOfCities();
                TspObservation[] observations = new TspObservation[nbrTours];
                for (int i = 0; i < nbrTours; i++) {
                    long start = System.nanoTime();
                    TspTour tour = algorithm.computeTour(data, i);
                    long end = System.nanoTime();
                    // (end - start) / 1000000 + "ms"
                    observations[i] = new TspObservation(algorithm, tour, end - start);
                }

                TspObservation minTour = observations[0];
                for (TspObservation observation : observations) {
                    if (observation.tour.length() < minTour.tour.length()) {
                        minTour = observation;
                    }
                }

                System.out.println("min Tour pour " + name + " avec " + algorithm.getClass() + " " + minTour);
            }
        }
    }
}