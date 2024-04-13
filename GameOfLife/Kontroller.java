class Kontroller {
    private Utsyn ut; // benytter interface, selv om jeg i dette programmet ikke har andre former for utsyn (View)
    private GameOfLife spillModell; // kunne benyttet et interface her ogsaa, men for enkelthets skyld dropper jeg det
    private int rader = 8; // DEFAULT-oppsett
    private int kolonner = 12; // DEFAULT-oppsett
    private boolean[][] status; // oversikt hvilke celler er levende
    private Klokke klokke = new Klokke();
    private Thread klokkeTraad;

    Kontroller (String[] args) {
        if (args.length == 2) { // endrer rader og kolonner kun hvis det er skrevet inn 2 gyldige* argumenter (*sjekkes i hovedprogram)
            rader = Integer.parseInt(args[0]);
            kolonner = Integer.parseInt(args[1]);
        }
        ut = new GUI();
        spillModell = new GameOfLife(rader, kolonner);
    } 

    public void init() { // Starter spillet

        // Lage en verden i modellen og hent status
        spillModell.lagVerden();
        status = spillModell.hentStatus();
                        
        // Starte Utsyn
        ut.init(this);
        ut.antall(spillModell.hentAntLevende());
        ut.tegn(status); // oppdaterer rutenettet med utgangsposisjonen for cellene
    }

    public int hentRader() {
        return rader;
    }

    public int hentKolonner() {
        return kolonner;
    }

    public void endreCelleStatus(int rad, int kolonne) {
        // oppdater modell
        int nyttAnt = spillModell.endreStatus(rad, kolonne);

        // hent oppdatering
        status = spillModell.hentStatus();

        // vise i Utsyn (tegner alt paa nytt) 
        /* MERKNAD 
            Jeg kunne alternativt hatt metoder for aa oppdatere enkeltknapp, 
            ikke hele rutenettet og antall, men jeg bruker heller en metode som 
            benyttes i programmet slik det var skrevet fra foer (for terminalvisning). 
        */ 
        ut.tegn(status); // tegn rutenett
        ut.antall(nyttAnt); // oppdater antall levende celler

    }
    
    public void oppdater() {
        status = spillModell.oppdater(); // Oppdatere spillModell til neste generasjon celler
        ut.tegn(status); // Tegne opp resultatet
        ut.antall(spillModell.hentAntLevende()); // Vise antall levende (oppdatert)
    }

    public void styrTiden(boolean oppstart) {
        if (oppstart) { // ved foerste klikk paa start, altsaa oppstart
            klokkeTraad = new Thread(klokke);
            klokkeTraad.start();
        } 
        else { // ellers vil klokken bytte tilstand, det vil si annenhver gang stoppe og starte (se kode Klokke)
            klokkeTraad.interrupt();
        }
    }

    class Klokke implements Runnable {

        @Override
        public void run() {
            while (true) {
                try{
                    Thread.sleep(2000); // 2 sekunder mellom hver oppdatering
                    oppdater();
                } catch(InterruptedException e1) { // vil stoppe naar traad blir avbrutt (det trykkes Stopp)...
                    try{
                        Thread.sleep(600000); // ... og vente inntil 10 minutter
                        System.out.println("Programmet avsluttet grunnet inaktivitet");
                        System.exit(1); // stoppe programmet helt
                    } catch(InterruptedException e2) {
                        // vil fortsette naar traad blir avbrutt paa nytt (det trykkes Start)
                    }
                }
            }
        }    
    }    
}
