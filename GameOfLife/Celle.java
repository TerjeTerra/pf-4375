class Celle {
    boolean levende = false;
    Celle [] naboer = new Celle[8];
    int antNaboer = 0;
    int antLevendeNaboer = 0;

    public void settLevende(){
        levende = true;
    }
    
    public void settDoed(){
        levende = false;
    }

    public boolean erLevende(){
        return levende;
    }

    public void leggTilNabo(Celle celle) {
        // legge til celle i arrayen naboer
        naboer[antNaboer] = celle;
        antNaboer ++; // oeker antNaboer
    }

    public void tellLevendeNaboer() {
        antLevendeNaboer = 0; //skal alltid være 0 foer opptelling
        for (int i = 0; i < antNaboer; i++) {
            if (naboer[i].erLevende()) antLevendeNaboer++;
        }
    }

    public void oppdaterStatus() {
        if (levende) {
            if (antLevendeNaboer < 2) levende = false;
            if (antLevendeNaboer > 3) levende = false;
            // i andre tilfeller fortsetter cellen aa leve
        }
        else if (antLevendeNaboer == 3) levende = true; // her er cellen doed fra foer
            // ellers forblir cellen doed
        
    }
    
}
