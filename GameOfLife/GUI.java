import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;

class GUI implements Utsyn {
    private Kontroller kontroller;
    private JFrame vindu;
    private JPanel panel;
    private int rader;
    private int kolonner;
    private JButton[][] celleKnapper;
    private JLabel antall = new JLabel(""); // Plass til aa fylle inn info om antall levende celler

    class CelleVelger implements ActionListener {
	    int rad, kol;
	    CelleVelger(int r, int k) {
			rad = r;  kol = k;
	    }

		@Override
		public void actionPerformed (ActionEvent e) {
			kontroller.endreCelleStatus(rad, kol);
		}
	}

    class NesteGenerasjon implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            kontroller.oppdater();

        }
    }

    @Override
    public void init(Kontroller k) {
        kontroller = k;
        rader = kontroller.hentRader();
        kolonner = kontroller.hentKolonner();

        JPanel rutenett = new JPanel();
        celleKnapper = new JButton[rader][kolonner];

        // Opprette vindu med mer (standard innledning)
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { System.exit(1); }	
	
        vindu = new JFrame("Game of Life");
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vindu.setMinimumSize(new Dimension(700, 400));
        
        // Knapper med ActionListeners og aktuelle klasser
        JButton b1 = new JButton("Start");
        class StartOgStopp implements ActionListener {
            private boolean oppstart = true;

            @Override
            public void actionPerformed (ActionEvent e) {
                kontroller.styrTiden(oppstart); // styre tiden, foerste gang er oppstart true
                oppstart = false; // deretter er oppstart false
                
                // Endre tekst paa knappen
                if (b1.getText().equals("Start")) {
                    b1.setText("Pause");
                } 
                else {
                    b1.setText("Start");
                }
            }
        }
        b1.addActionListener(new StartOgStopp());
        
        JButton b2 = new JButton("Avslutt");
        class Avslutt implements ActionListener {
            @Override
            public void actionPerformed (ActionEvent e) {
                System.exit(0);
            }
        }
        b2.addActionListener(new Avslutt());
        
        // Tegneflate
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Menylinje paa toppen
        JPanel meny = new JPanel();
        meny.add(antall);
        meny.add(b1);
        meny.add(b2);
        panel.add(meny, BorderLayout.NORTH);

        // Rutenett (spillbrett) nederst
        rutenett.setLayout(new GridLayout(rader, kolonner));

        // Lag knapp for hver kolonne i hver rad
        for (int rad = 0; rad < rader; rad++ ) {
            for (int kol = 0; kol < kolonner; kol++) {
                JButton b = new JButton("");
                celleKnapper[rad][kol] = b;
                b.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
                b.addActionListener(new CelleVelger(rad,kol));
                rutenett.add(b);
            }
        }
     
        panel.add(rutenett, BorderLayout.CENTER);
        vindu.add(panel);

        // Pakk JFrame objektet og gjor det synlig
        vindu.pack();
        vindu.setLocationRelativeTo(null);
        vindu.setVisible(true);
    }

    @Override
    public void tegn(boolean[][] status) {
        // Faste symboler
        char levendeSymbol = '@';
        char doedSymbol = ' ';

        // Oppdatere tekst på knappene i hver kolonne i hver rad
        for (int rad = 0; rad < rader; rad++ ) {
            for (int kol = 0; kol < kolonner; kol++) {
                if (status[rad][kol]) celleKnapper[rad][kol].setText(levendeSymbol + "");
                else celleKnapper[rad][kol].setText(doedSymbol + "");
            }
        }
    }

    @Override
    public void antall(int ant) {
        antall.setText("Ant. levende celler: " + ant);
    }
}