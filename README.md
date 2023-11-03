# SIO_23-24_TP1

Exemple d'output console généré par le programme. Le ```averageTime``` peut varier selon la machine sur laquelle le code est exécuté.

Légende :
- ```file``` est le nom du fichier 
- ```algorithm``` est l'algorithm utilisé
- ```minLength``` est la longueur minimale trouvée
- ```averageLength``` est la longueur moyenne trouvée
- ```minRatio``` est le ratio entre la longueur optimale et la longueur minimale trouvée
- ```averageRatio``` est le ratio entre la longueur optimale et la longueur moyenne trouvée
- ```averageTime``` en ```ms``` est le temps moyen nécessaire pour calculer un tour

| algorithm                 | minRatio | averageRatio | averageTime |
|:--------------------------|---------:|-------------:|------------:|
| NearestNeighbor           | 1,174271 |     1,224445 |   16,668824 |
| DoubleEndsNearestNeighbor | 1,158471 |     1,194973 |   20,045331 |

| file    | algorithm                 | minLength | averageLength | minRatio | averageRatio | averageTime |
|:--------|:--------------------------|----------:|--------------:|---------:|-------------:|------------:|
| att532  | NearestNeighbor           |    101261 | 105406,556391 | 1,167556 |     1,215355 |    0,542433 |
| att532  | DoubleEndsNearestNeighbor |     99417 | 102637,689850 | 1,146295 |     1,183430 |    0,747343 |
| rat575  | NearestNeighbor           |      7878 |   8167,643478 | 1,163148 |     1,205912 |    0,508262 |
| rat575  | DoubleEndsNearestNeighbor |      7788 |   8007,109565 | 1,149860 |     1,182210 |    0,626684 |
| rl1889  | NearestNeighbor           |    368709 | 386674,676548 | 1,164825 |     1,221582 |   34,575047 |
| rl1889  | DoubleEndsNearestNeighbor |    367299 | 378990,666490 | 1,160370 |     1,197307 |   44,567601 |
| u574    | NearestNeighbor           |     44061 |  46323,010453 | 1,193903 |     1,255196 |    1,136667 |
| u574    | DoubleEndsNearestNeighbor |     43263 |  44730,273519 | 1,172280 |     1,212038 |    0,896798 |
| u1817   | NearestNeighbor           |     66979 |  69504,077600 | 1,170941 |     1,215085 |   35,504831 |
| u1817   | DoubleEndsNearestNeighbor |     65870 |  67818,374794 | 1,151553 |     1,185615 |   38,923198 |
| vm1748  | NearestNeighbor           |    398904 | 415155,640732 | 1,185253 |     1,233541 |   27,745701 |
| vm1748  | DoubleEndsNearestNeighbor |    393927 | 406976,049771 | 1,170465 |     1,209237 |   34,510365 |

