/**
 * Authors : Jeremiah Steiner & Simon Guggisberg
 */

package sio.groupD;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspParsingException;
import sio.tsp.TspTour;

import java.io.FileNotFoundException;

/**
 * Classe Main comprenant les différents paramètres et lançant les différentes fonctions de l'application
 */
public final class Main {

    /**
     * Record contenant les méta informations pour un problème TSP
     *
     * @param name          le nom d'un fichier contenant les données du problème
     * @param optimalLength la longueur optimale correspondant aux données du problème
     */
    private record TspMetaData(String name, int optimalLength) {
    }

    /**
     * Record contenant le résumé de résultats
     *
     * @param minLength     longueur minimale trouvée
     * @param averageLength longueur moyenne trouvée
     * @param minRatio      ratio entre longueur optimale et longueur minimale trouvée
     * @param averageRatio  ratio entre longueur optimale et longueur moyenne trouvée
     * @param averageTime   temps moyen nécessaire pour calculer un tour, en ms
     */
    private record TspObservationSummary(long minLength, double averageLength, double minRatio, double averageRatio, double averageTime) {
    }

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
     * Calcule un résumé de résultats par metadata par algorithme
     *
     * @param filesFolder    le dossier dans lequel se trouvent les fichiers contenant les données
     * @param filesExtension l'extension de fichier dans lesquels se trouvent les données
     * @return le résumé des résultats, par algorithme, par metadata
     * @throws FileNotFoundException si les fichiers indiqués n'existent pas
     * @throws TspParsingException   si le format du fichier ne correspond pas à ce qui est attendu
     */
    private static TspObservationSummary[][] computePerMetadataPerAlgorithm(String filesFolder, String filesExtension) throws FileNotFoundException, TspParsingException {
        record TspObservation(TspConstructiveHeuristic algorithm,
                              TspTour tour, long runTime) {
        }

        TspObservationSummary[][] summaryData = new TspObservationSummary[METADATA.length][ALGORITHMS.length];
        // Itère sur les fichiers
        for (int dataIndex = 0; dataIndex < METADATA.length; dataIndex++) {
            TspMetaData metaData = METADATA[dataIndex];
            TspData data = null;
            data = TspData.fromFile(filesFolder + "/" + metaData.name + filesExtension);
            System.out.println("computing " + metaData.name);

            // Itère sur les algorithmes
            for (int algoIndex = 0; algoIndex < ALGORITHMS.length; algoIndex++) {
                if (data.getNumberOfCities() > 1000) {
                    summaryData[dataIndex][algoIndex] = new TspObservationSummary(
                            0,0,0,0,0);
                    continue;
                }

                TspConstructiveHeuristic algorithm = ALGORITHMS[algoIndex];
                int nbrTours = data.getNumberOfCities();
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
                summaryData[dataIndex][algoIndex] = new TspObservationSummary(
                        minTour.tour.length(),
                        average,
                        (double) minTour.tour.length() / metaData.optimalLength,
                        average / metaData.optimalLength,
                        totalTime / observations.length
                );
            }
        }

        return summaryData;
    }

    /**
     * Imprime les résultats relevants par metadata par algorithme
     *
     * @param summaries le résumé des résultats
     */
    private static void printPerMetadataPerAlgorithm(TspObservationSummary[][] summaries) {
        // Imprime le header du tableau par fichier par algorithme
        String formatString = "| %-7s | %-25s | %9d | %13f | %8f | %12f | %11f |%n";
        String line = "+---------+---------------------------+-----------+---------------+----------+--------------+-------------+%n";
        System.out.format("| file    | algorithm                 | minLength | averageLength | minRatio | averageRatio | averageTime |%n");
        System.out.format(line);

        // Imprime le contenu du tableau par fichier par algorithme
        for (int dataIndex = 0; dataIndex < METADATA.length; dataIndex++) {
            for (int algoIndex = 0; algoIndex < ALGORITHMS.length; algoIndex++) {
                System.out.format(formatString,
                        METADATA[dataIndex].name,
                        ALGORITHMS[algoIndex].getClass().getSimpleName(),
                        summaries[dataIndex][algoIndex].minLength,
                        summaries[dataIndex][algoIndex].averageLength,
                        summaries[dataIndex][algoIndex].minRatio,
                        summaries[dataIndex][algoIndex].averageRatio,
                        summaries[dataIndex][algoIndex].averageTime
                );
            }
        }

        System.out.format(line);
    }

    /**
     * Imprime la moyenne des résultats relevants par algorithme
     *
     * @param summaries les résultats, par algorithme, par metadata et par résultats relevants
     */
    private static void printPerAlgorithm(TspObservationSummary[][] summaries) {
        // Imprime le header du tableau par algorithme
        String formatString = "| %-25s | %-8f | %-12f | %-11f |%n";
        String line = "+---------------------------+----------+--------------+-------------+%n";
        System.out.format("| algorithm                 | minRatio | averageRatio | averageTime |%n");
        System.out.format(line);

        // Imprime le contenu du tableau par algorithme
        for (int algoIndex = 0; algoIndex < ALGORITHMS.length; algoIndex++) {
            // Calcule la moyenne des résultats relevants par algorithme
            double[] summaryLine = new double[NBR_RELEVANT_RESULTS];
            for (int dataIndex = 0; dataIndex < METADATA.length; dataIndex++) {
                summaryLine[0] += summaries[dataIndex][algoIndex].minRatio;
                summaryLine[1] += summaries[dataIndex][algoIndex].averageRatio;
                summaryLine[2] += summaries[dataIndex][algoIndex].averageTime;
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

        System.out.format(line);
    }

    public static void main(String[] args) {
        /*
        DoubleEndsNearestNeighbor algo = new DoubleEndsNearestNeighbor();
        try {
            algo.computeTour(TspData.fromFile("data/test4.dat"), 0);
        }
        catch (Exception e) {
            System.out.println("fuck you \n" + e);
        }
        */
        // Calcule TspObservationSummary par fichier par algorithme
        TspObservationSummary[][] summaryData;
        try {
            summaryData = computePerMetadataPerAlgorithm("data", ".dat");
        } catch (FileNotFoundException e) {
            System.out.println("A TSP data files could not be found, please ensure either the folder or the extension parameters are correct.");
            return;
        } catch (TspParsingException e) {
            System.out.println("A TSP data file does not conform to the expected format.");
            return;
        }

        System.out.println();
        printPerAlgorithm(summaryData);
        System.out.println();
        printPerMetadataPerAlgorithm(summaryData);
    }
}