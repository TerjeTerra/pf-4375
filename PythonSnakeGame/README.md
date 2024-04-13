# PythonSnakeGame

Welcome to a classic game of Snake! This project started as a tutor-assigned task for teachers learning basic programming. 
We used Python Standard Library with Turtle for UI. The original code sample can be found in originalSnakeGeeks.py, available here: https://www.geeksforgeeks.org/create-a-snake-game-using-turtle-in-python/

I developed the game further as a teacher for my own students (who also programmed their own snake games). 
In my spare time as a student of informatics at the University of Oslo, I continued refining the game.

Feel free to use this project as a showcase and enjoy playing Snake!

## How to play
Simply run the file 'main.py' with Python.

This will start a gameplay introduction and setup. 

Enjoy!

## Requirements

This project was developed and tested with Python 3.12.1. 

No additional libraries are required beyond the Python Standard Library.

## Version
Version: 4.1

## History of development:

**Note**: Development history below is provided in Norwegian only.

### Versjon 4.1

- oppdatert readme.md
- engelsk ved spillets start og i kodekommentarer

### Versjon 4.0

- klar for oppdatert showcase
- velg antall spillere fra startmeny
- unngaa at mat plasseres under en slange
- spillere kan krasje med hverandre
- utviklet scoring i 2-player-mode og display av meldinger
- highscore oppdateres bare mellom hvert spill

### Versjon 3.0

- klargjort for foerste showcase
- forbedret hvordan hastighet oeker gjennom spillet (opp til en viss grense)

### Versjon 2.7

- slangen starter med hale, ikke bare med hode
- refaktorering: samlet game og world i egne filer og klasser

### Versjon 2.6

- flyttet score utenfor rammen
- flyttet og samlet noen metoder inn i Snakes-klassen (f.eks. move())

### Versjon 2.5 (januar 2024)

- funksjoner for pause, avslutte og fortsette spill lagt til
- kan nå styre slangen med piltaster i tillegg til ASDW

### Tidligere versjoner

- laget variabler for å kontrollere størrelsen på spilleområdet og gjøre det 
  enkelt med ulike størrelser på spillområdet
- tegnet ytterkanter
- fjernet blå mat som en mulighet (usynlig på blå bakgrunn)
- mat kan ikke dukke opp for nærme slangehodet i starten av et spill
- mat kan ikke plasseres under halen på slangen (er IKKE implementert i alle 
  senere versjoner)
- underveis: sortert deler av koden i funksjoner og laget flere funksjoner der
  det virket naturlig og fornuftig
- startet på utvikling i retning objekt-orientert programmering
- legge ut mer mat samtidig
- utvidet poengsystemer (ulik mat/poeng)
- endre fart underveis
- brukerstyrt mulighet for å avslutte spillet med Q
