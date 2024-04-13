public class GameOfLife {
    int rader;
    int kolonner;
    Verden verden;

    public GameOfLife(int rader, int kolonner){
        this.rader = rader;
        this.kolonner = kolonner;
    }

    public void lagVerden() {
        verden = new Verden(rader, kolonner);
    }

    public Verden hentVerden() {
        return verden;
    }

    // Metode for aa hente naaværende cellestatus. True = levende.
    public boolean[][] hentStatus() {
        boolean[][] status = new boolean[rader][kolonner];
        Celle[][] cellene = verden.hentRutenett().hentCellene();
        for (int r = 0; r < rader; r++) {
            for (int k = 0; k < kolonner; k++) {
                if (cellene[r][k].erLevende()) status[r][k] = true;
            }
        }
        return status;
    }

    public boolean[][] oppdater() {
        verden.oppdatering();
        return hentStatus();
    }

    public int hentAntLevende() {
        return verden.hentRutenett().antallLevende();
    }

    // endre status for en enkelt celle
    public int endreStatus(int rad, int kol) {
        Celle c = verden.hentRutenett().hentCelle(rad, kol);
        if (c.erLevende()) c.settDoed();
        else c.settLevende();
        return verden.hentRutenett().antallLevende();
    }
}
