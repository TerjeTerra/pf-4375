# @ Game of Life @
Et studentprosjekt skrevet våren 2023. Spillet er en visualisering av Game of Life, der celler utvikler seg etter regler basert på antall levende  naboceller, visualisert som '@'.

## Hvordan starte spillet
To muligheter:
* Kompilere og kjøre Hovedprogram (merk navn!)
```bash
javac *.java
java Hovedprogram [rader] [kolonner]
``` 

* Åpne den kjørbare jar-filen (se Krav)
```bash
java -jar GameOfLife.jar [rader] [kolonner]
```


I begge tilfeller kan heltallsparametrene (antall) rader og kolonner sløyfes. Da starter spillet med et standard 8 x 12 rutenett.

### Krav
Java Runtime Environment (JRE) kreves for å kjøre JAR file. Programmet er testet med JRE 17.0.10.


## Hvordan spille
Spillet kan startes, pauses og avsluttes med knappene. Len deg tilbake å se hvordan livet utvikler seg. Er du ikke fornøyd med utviklingen, kan du klikke på celler for å endre status (død eller levende) når som helst. 
