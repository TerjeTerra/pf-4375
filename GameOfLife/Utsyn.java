interface Utsyn {
    void init(Kontroller k); // oppstart
    void tegn(boolean[][] s); // skal vise rutenett basert på en status s over hvilke celler som lever
    void antall(int ant); // vise antallet celler som lever
}
