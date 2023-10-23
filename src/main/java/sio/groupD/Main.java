package sio.groupD;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspTour;

public final class Main {

    private record TspMetaData (String name, int optimalLength) { }
    private record TspObservation(TspConstructiveHeuristic algorithm, TspTour tour, long runTime) { }

    // TODO remove traces of MAX_VALUE_TEST for rendu
    private static final int MAX_VALUE_TEST = 1000;

    private static final int NS_2_MS = 1000000;

    public static void main(String[] args) {
        // TODO
        //  - Affichage des statistiques dans la classe Main ;
        //  - Documentation abondante des classes comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        TspMetaData[] metaDatas = {
                new TspMetaData("att532", 86729),
                new TspMetaData("rat575", 6773),
                new TspMetaData("rl1889", 316536),
                new TspMetaData("u574", 36905),
                new TspMetaData("u1817", 57201),
                new TspMetaData("vm1748", 336556),
        };
        TspConstructiveHeuristic[] algorithms = {new NearestNeighbor(), new DoubleEndsNearestNeighbor()};

        String formatString = "| %-6s | %-25s | %-9d | %-14f | %-8f | %-12f | %-11f |%n";
        String line =     "+--------+---------------------------+-----------+----------------+----------+--------------+-------------+%n";
        System.out.format(line);
        System.out.format("| file   | algorithm                 | minLength | averageLength  | minRatio | averageRatio | averageTime |%n");
        System.out.format(line);

        double[][][] summaryData = new double[algorithms.length][metaDatas.length][3];

        for (int dataIndex = 0; dataIndex < metaDatas.length; dataIndex++) {
            TspMetaData metaData  = metaDatas[dataIndex];
            TspData data = null;
            try {
                data = TspData.fromFile("data/" + metaData.name + ".dat");
            } catch (Exception e) {
                System.out.println("Something wrong happened while trying to read data from file : " + e);
                return;
            }

            for (int algoIndex = 0; algoIndex < algorithms.length; algoIndex++) {
                TspConstructiveHeuristic algorithm = algorithms[algoIndex];
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
                    observations[j] = new TspObservation(algorithm, tour, end - start);
                }

                // Retrieves average and min data from all observations
                TspObservation minTour = observations[0];
                long totalLength = 0;
                double totalTime = 0;
                for (TspObservation observation : observations) {
                    totalLength += observation.tour.length();
                    totalTime += (double) observation.runTime / NS_2_MS;
                    if (observation.tour.length() < minTour.tour.length()) {
                        minTour = observation;
                    }
                }

                double average = (double) totalLength / observations.length;

                // Saves meaningful summarized data
                summaryData[algoIndex][dataIndex] = new double[]{
                        (double) minTour.tour.length() / metaData.optimalLength,
                        average / metaData.optimalLength,
                        totalTime / observations.length
                };

                System.out.format(formatString,
                        metaData.name,
                        algorithm.getClass().getSimpleName(),
                        minTour.tour.length(),
                        average,
                        summaryData[algoIndex][dataIndex][0],
                        summaryData[algoIndex][dataIndex][1],
                        summaryData[algoIndex][dataIndex][2]
                );
            }
        }

        System.out.format(line);

        System.out.println("\n\n");

        formatString = "| %-25s | %-8f | %-12f | %-11f |%n";
        line =            "+---------------------------+----------+--------------+-------------+%n";
        System.out.format(line);
        System.out.format("| algorithm                 | minRatio | averageRatio | averageTime |%n");
        for (int algoIndex = 0; algoIndex < algorithms.length; algoIndex++) {
            int nbrSummary = 3;
            double[] summaryLine = new double[nbrSummary];
            for (int dataIndex = 0; dataIndex < metaDatas.length; dataIndex++) {
                for (int i = 0; i < nbrSummary; i++) {
                    summaryLine[i] += summaryData[algoIndex][dataIndex][i];
                }
            }

            for (int i = 0; i < nbrSummary; i++) {
                summaryLine[i] /= metaDatas.length;
            }

            System.out.format(formatString,
                    algorithms[algoIndex].getClass().getSimpleName(),
                    summaryLine[0],
                    summaryLine[1],
                    summaryLine[2]
            );
        }
        System.out.format(line);
    }
}