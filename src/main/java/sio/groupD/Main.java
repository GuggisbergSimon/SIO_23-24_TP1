package sio.groupD;

import sio.tsp.TspData;
import sio.tsp.TspParsingException;
import sio.tsp.TspTour;

import java.io.FileNotFoundException;

public final class Main {
  public static void main(String[] args){
    // TODO
    //  - Renommage du package ;
    //  - Implémentation des classes NearestNeighbor et DoubleEndsNearestNeighbor ;
    //  - Affichage des statistiques dans la classe Main ;
    //  - Documentation abondante des classes comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.

    // Longueurs optimales :
    // att532 : 86729
    // rat575 : 6773
    // rl1889 : 316536
    // u574   : 36905
    // u1817  : 57201
    // vm1748 : 336556

    NearestNeighbor nn_att532 = new NearestNeighbor();
    try
    {
      TspTour tmp = nn_att532.computeTour(TspData.fromFile("data/att532.dat"), 0);
      System.out.println(tmp.toString());
    }
    catch (Exception e)
    {
      System.out.println(e);
    }


    // Exemple de lecture d'un jeu de données :
    // TspData data = TspData.fromFile("data/att532.dat");
  }
}