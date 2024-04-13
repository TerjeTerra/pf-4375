class Rutenett {
    int antRader;
    int antKolonner;
    Celle[][] rutene;

    public Rutenett(int rader, int kolonner) {
        antRader = rader;
        antKolonner = kolonner;
        rutene = new Celle[antRader][antKolonner];
    }

    public Celle[][] hentCellene() {
        return rutene;
    }

    public void lagCelle(int rad, int kol) {
        Celle celle = new Celle();
        rutene[rad][kol] = celle;
        if (Math.random() <= 0.3333) celle.settLevende(); //Hver tredje celle lever
    }

    public void fyllMedTilfeldigeCeller() {
        for (int r = 0; r < antRader; r++){
            for (int k = 0; k < antKolonner; k++){
                lagCelle(r, k);
            }
        }
    }

    public Celle hentCelle(int rad, int kolonne) {
        if (rad < 0 || rad >= antRader) return null;
        if (kolonne < 0 || kolonne >= antKolonner) return null;
        else return rutene[rad][kolonne];

    }

   public void settNaboer(int rad, int kolonne) {
        Celle nabo;
        int r = rad;
        int k = kolonne;
        for (int i = -1; i < 2; i++) { //starter aa se paa raden ovenfor
            for (int j = -1; j < 2; j++) { //deretter kolonnene fra venstre
                nabo = hentCelle(r+i, k+j);
                if (nabo != null && !(i == 0 && j == 0)) { //ikke legge til tomme plasser eller seg selv
                    rutene[r][k].leggTilNabo(nabo);
                }
            }
        }
    }
               
    public void kobleAlleCeller() {
        for (int rad = 0; rad < antRader; rad++) { 
            for (int kol = 0; kol < antKolonner; kol++) {
                settNaboer(rad, kol);
            }
        }
    }

    public int antallLevende() {
        int opptelling = 0;
        for (int rad = 0; rad < antRader; rad++) { 
            for (int kol = 0; kol < antKolonner; kol++) {
                if (rutene[rad][kol].erLevende()) opptelling++;
            }
        }
        return opptelling;
    }
}
