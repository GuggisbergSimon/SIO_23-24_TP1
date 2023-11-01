package sio.groupD;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspParsingException;
import sio.tsp.TspTour;
import java.io.FileNotFoundException;

public final class Main {

    private record TspMetaData (String name, int optimalLength) { }

    // TODO remove traces of MAX_VALUE_TEST for rendu
    private static final int MAX_VALUE_TEST = 1000;
    private static final int NS_2_MS = 1000000;
    private static final int NBR_RELEVANT_RESULTS = 3;
    private static final TspConstructiveHeuristic[] ALGORITHMS = {new NearestNeighbor(), new DoubleEndsNearestNeighbor()};
    private static final TspMetaData[] METADATA = {
            new TspMetaData("att532", 86729),
            new TspMetaData("rat575", 6773),
            new TspMetaData("rl1889", 316536),
            new TspMetaData("u574", 36905),
            new TspMetaData("u1817", 57201),
            new TspMetaData("vm1748", 336556),
    };

    /**
     * Calcule et imprime les résultats relevants par metada par algorithme
     * @param formatString le format dans lequel il faut imprimer les résultats
     * @param filesFolder le dossier dans lequel se trouvent les fichiers contenant les données
     * @param filesExtension l'extension de fichier dans lesquels se trouvent les données
     * @return les résultats, par algorithme, par metadata et par résultats relevants
     * @throws FileNotFoundException si les fichiers indiqués n'existent pas
     * @throws TspParsingException si le format du fichier ne correspond pas à ce qui est attendu
     */
    private static double[][][] computeAndPrintPerMetadataPerAlgorithm(String formatString, String filesFolder, String filesExtension) throws FileNotFoundException, TspParsingException {
        record TspObservation(TspConstructiveHeuristic algorithm, TspTour tour, long runTime) { }

        double[][][] summaryData = new double[ALGORITHMS.length][METADATA.length][NBR_RELEVANT_RESULTS];
        // Itère sur les fichiers
        for (int dataIndex = 0; dataIndex < METADATA.length; dataIndex++) {
            TspMetaData metaData  = METADATA[dataIndex];
            TspData data = null;
            data = TspData.fromFile(filesFolder + "/" + metaData.name + filesExtension);

            // Itère sur les algorithmes
            for (int algoIndex = 0; algoIndex < ALGORITHMS.length; algoIndex++) {
                TspConstructiveHeuristic algorithm = ALGORITHMS[algoIndex];
                int nbrTours = data.getNumberOfCities();

                // TODO remove traces of MAX_VALUE_TEST for rendu
                if (nbrTours > MAX_VALUE_TEST) {
                    continue;
                }

                // Calcule tous les tours possibles et les enregistre sous forme d'observation
                TspObservation[] observations = new TspObservation[nbrTours];
                for (int j = 0; j < nbrTours; j++) {
                    long start = System.nanoTime();
                    TspTour tour = algorithm.computeTour(data, j);
                    long end = System.nanoTime();
                    observations[j] = new TspObservation(algorithm, tour, end - start);
                }

                // Calcule la moyenne et la valeur minimale parmi toute les observations
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

                // Sauvegarde les résultats relevants par fichier par algorithme
                summaryData[algoIndex][dataIndex] = new double[]{
                        (double) minTour.tour.length() / metaData.optimalLength,
                        average / metaData.optimalLength,
                        totalTime / observations.length
                };

                // Imprime les résultats par fichier par algorithme
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

        return summaryData;
    }

    /**
     * Imprime la moyenne des résultats relevants par algorithme
     * @param data les résultats, par algorithme, par metadata et par résultats relevants
     * @param formatString le format dans lequel il faut imprimer les résultats
     */
    private static void printPerAlgorithm(double[][][] data, String formatString) {
        for (int algoIndex = 0; algoIndex < ALGORITHMS.length; algoIndex++) {
            // Calcule la moyenne des résultats relevants par algorithme
            double[] summaryLine = new double[NBR_RELEVANT_RESULTS];
            for (int dataIndex = 0; dataIndex < METADATA.length; dataIndex++) {
                for (int i = 0; i < NBR_RELEVANT_RESULTS; i++) {
                    summaryLine[i] += data[algoIndex][dataIndex][i];
                }
            }

            for (int i = 0; i < NBR_RELEVANT_RESULTS; i++) {
                summaryLine[i] /= METADATA.length;
            }

            // Imprime la moyenne des résultats relevants par algorithme
            System.out.format(formatString,
                    ALGORITHMS[algoIndex].getClass().getSimpleName(),
                    summaryLine[0],
                    summaryLine[1],
                    summaryLine[2]
            );
        }
    }

    public static void main(String[] args) {
        // TODO
        //  - Documentation abondante des classes comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        // Imprime le header du premier tableau
        String formatString = "| %-6s | %-25s | %-9d | %-14f | %-8f | %-12f | %-11f |%n";
        String line =     "+--------+---------------------------+-----------+----------------+----------+--------------+-------------+%n";
        System.out.format(line);
        System.out.format("| file   | algorithm                 | minLength | averageLength  | minRatio | averageRatio | averageTime |%n");
        System.out.format(line);

        double[][][] summaryData;
        try {
            summaryData = computeAndPrintPerMetadataPerAlgorithm(formatString, "data", ".dat");
        }
        catch (FileNotFoundException e) {
            System.out.println("A TSP data files could not be found, please ensure either the folder or the extension parameters are correct.");
            return;
        }
        catch (TspParsingException e) {
            System.out.println("A TSP data file does not conform to the expected format.");
            return;
        }

        System.out.format(line);
        System.out.println();

        // Imprime le header du second tableau
        formatString = "| %-25s | %-8f | %-12f | %-11f |%n";
        line =            "+---------------------------+----------+--------------+-------------+%n";
        System.out.format(line);
        System.out.format("| algorithm                 | minRatio | averageRatio | averageTime |%n");

        printPerAlgorithm(summaryData, formatString);

        System.out.format(line);
    }
}