/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/** La classe Jeu a deux fonctions 
 *  (1) Gérer les aspects du jeu : condition de défaite, victoire, nombre de vies
 *  (2) Gérer les coordonnées des entités du monde : déplacements, collisions, perception des entités, ... 
 *
 * @author freder
 */
public class Jeu extends Observable implements Runnable {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;

    private Pacman pm;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    private HashMap<Mur, Point> mapMurs = new  HashMap<Mur, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Mur[][] grilleMurs = new Mur[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    
    // TODO : ajouter les murs, couloir, PacGums, et adapter l'ensemble des fonctions (prévoir le raffraichissement également du côté de la vue)
    
    
    public Jeu() {
        initialisationDesEntites();
        initialisationDesMurs();
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Mur[][] getGrilleMurs() {
        return grilleMurs;
    }
    
    public Pacman getPacman() {
        return pm;
    }
    
    private void initialisationDesEntites() {
        //  Initialisation du pacman
        pm = new Pacman(this, 3, false);
        grilleEntites[2][2] = pm;
        map.put(pm, new Point(2, 2));
        
        // Initialisation des fantomes
        Fantome bleu = new Fantome(this, "bleu");
        Fantome rose = new Fantome(this, "rose");
        Fantome rouge = new Fantome(this, "rouge");
        Fantome orange = new Fantome(this, "orange");
        grilleEntites[10][10] = bleu;
        grilleEntites[10][9] = rose;
        grilleEntites[9][10] = rouge;
        grilleEntites[9][9] = orange;
        map.put(bleu, new Point(10, 10));
        map.put(rose, new Point(10, 9));
        map.put(rouge, new Point(9, 10));
        map.put(orange, new Point(9, 9));
        
    }
    public void initialisationDesMurs(){
        Mur mur;
        // A1
        mur = new Mur(this, "angle", 1);
        grilleMurs[0][0] = mur;
        mapMurs.put(mur, new Point(0,0));
        
        // T1
        mur = new Mur(this, "angle", 2);
        grilleMurs[SIZE_X-1][0] = mur;
        mapMurs.put(mur, new Point(SIZE_X-1,0));
        mur = new Mur(this, "angle", 3);
        
        // A20
        grilleMurs[SIZE_X-1][SIZE_Y-1] = mur;
        mapMurs.put(mur, new Point(SIZE_X-1,SIZE_Y-1));
        
        // T20
        mur = new Mur(this, "angle", 4);
        grilleMurs[0][SIZE_Y-1] = mur;
        mapMurs.put(mur, new Point(0,SIZE_Y-1));
        
        // B1 -> I1
        for(int i = 1; i < 9; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][0] = mur;
            mapMurs.put(mur, new Point(i,0));
        }
        
        // J1 -> K1
        for(int i = 9; i < 12; i++){
            mur = new Mur(this, "cote", 2);
            grilleMurs[i][0] = mur;
            mapMurs.put(mur, new Point(i,0));
        }
        
        // J2
        mur = new Mur(this, "cote", 1);
        grilleMurs[9][1] = mur;
        mapMurs.put(mur, new Point(9,1));
        
        // K2
        mur = new Mur(this, "cote", 3);
        grilleMurs[10][1] = mur;
        mapMurs.put(mur, new Point(10,1));
        
        // J3
        mur = new Mur(this, "angle", 4);
        grilleMurs[9][2] = mur;
        mapMurs.put(mur, new Point(9,2));
        
        // K3
        mur = new Mur(this, "angle", 3);
        grilleMurs[10][2] = mur;
        mapMurs.put(mur, new Point(10,2));
 
        // L1 -> S1
        for(int i = 11; i < SIZE_Y-1; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][0] = mur;
            mapMurs.put(mur, new Point(i,0));
        }
        
        // B20 -> S20
        for(int i = 1; i < SIZE_X-1; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][SIZE_Y-1] = mur;
            mapMurs.put(mur, new Point(i,SIZE_Y));
        }
        
        // A2 -> A6
        for(int i = 1; i < 6; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[0][i] = mur;
            mapMurs.put(mur, new Point(0,i));
        }
        
        // A16 -> A19
        for(int i = 15; i < SIZE_Y-1; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[0][i] = mur;
            mapMurs.put(mur, new Point(0,i));
        }
        
        // B7 -> E7
        for(int i = 1; i < 5; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][6] = mur;
            mapMurs.put(mur, new Point(i,6));
        }
        
        // A7
        mur = new Mur(this, "angle", 4);
        grilleMurs[0][6] = mur;
        mapMurs.put(mur, new Point(0,6));
        
        // F7
        mur = new Mur(this, "cote", 3);
        grilleMurs[5][6] = mur;
        mapMurs.put(mur, new Point(5,6));
        
        // F6
        mur = new Mur(this, "droit", 1);
        grilleMurs[5][5] = mur;
        mapMurs.put(mur, new Point(5,5));
        
        // F5
        mur = new Mur(this, "fin", 1);
        grilleMurs[5][4] = mur;
        mapMurs.put(mur, new Point(5,4));
        
        // A10 -> E10
        for(int i = 0; i < 5; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][9] = mur;
            mapMurs.put(mur, new Point(i,9));
        }
        
        // F10
        mur = new Mur(this, "angle", 3);
        grilleMurs[5][9] = mur;
        mapMurs.put(mur, new Point(5,9));
        
        // A12 -> E12
        for(int i = 0; i < 5; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][11] = mur;
            mapMurs.put(mur, new Point(i,11));
        }
        
        // B15 -> E15
        for(int i = 1; i < 5; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][14] = mur;
            mapMurs.put(mur, new Point(i,14));
        }
        
        // F12
        mur = new Mur(this, "angle", 2);
        grilleMurs[5][11] = mur;
        mapMurs.put(mur, new Point(0,14));
        
        // A15
        mur = new Mur(this, "angle", 1);
        grilleMurs[0][14] = mur;
        mapMurs.put(mur, new Point(0,14));
        
        // F15
        mur = new Mur(this, "angle", 3);
        grilleMurs[5][14] = mur;
        mapMurs.put(mur, new Point(0,14));
        
        // F8 F9
        for(int i = 7; i < 9; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[5][i] = mur;
            mapMurs.put(mur, new Point(5,i));
        }
        
        // F13 F14
        for(int i = 12; i < 14; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[5][i] = mur;
            mapMurs.put(mur, new Point(5,i));
        }
        
        // T2 -> T6
        for(int i = 1; i < 6; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[SIZE_X-1][i] = mur;
            mapMurs.put(mur, new Point(0,i));
        }
        
        // T16 -> T19
        for(int i = 15; i < SIZE_Y-1; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[SIZE_X-1][i] = mur;
            mapMurs.put(mur, new Point(0,i));
        }
        
        // P7 -> S7
        for(int i = 15; i < 19; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][6] = mur;
            mapMurs.put(mur, new Point(i,6));
        }
        
        // P10 -> T10
        for(int i = 15; i < 20; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][9] = mur;
            mapMurs.put(mur, new Point(i,9));
        }
        
        // T7
        mur = new Mur(this, "angle", 3);
        grilleMurs[SIZE_X-1][6] = mur;
        mapMurs.put(mur, new Point(SIZE_X-1,6));
        
        // O7
        mur = new Mur(this, "angle", 1);
        grilleMurs[14][6] = mur;
        mapMurs.put(mur, new Point(14,6));
        
        // O10
        mur = new Mur(this, "angle", 4);
        grilleMurs[14][9] = mur;
        mapMurs.put(mur, new Point(14,9));
        
        // P12 -> T12
        for(int i = 15; i < 20; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][11] = mur;
            mapMurs.put(mur, new Point(i,11));
        }
        
        // P15 -> S15
        for(int i = 15; i < 19; i++){
            mur = new Mur(this, "droit", 2);
            grilleMurs[i][14] = mur;
            mapMurs.put(mur, new Point(i,14));
        }
        
        // O12
        mur = new Mur(this, "angle", 1);
        grilleMurs[14][11] = mur;
        mapMurs.put(mur, new Point(14,11));
        
        // O15
        mur = new Mur(this, "angle", 4);
        grilleMurs[14][14] = mur;
        mapMurs.put(mur, new Point(14,14));
        
        // T15
        mur = new Mur(this, "angle", 2);
        grilleMurs[SIZE_X-1][14] = mur;
        mapMurs.put(mur, new Point(14,16));
        
        // O8 09
        for(int i = 7; i < 9; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[14][i] = mur;
            mapMurs.put(mur, new Point(14,i));
        }
        
        // O13 O14
        for(int i = 12; i < 14; i++){
            mur = new Mur(this, "droit", 1);
            grilleMurs[14][i] = mur;
            mapMurs.put(mur, new Point(14,i));
        }
        
        // C3
        mur = new Mur(this, "fin", 4);
        grilleMurs[2][2] = mur;
        mapMurs.put(mur, new Point(2,2));
        
        // D3
        mur = new Mur(this, "fin", 2);
        grilleMurs[3][2] = mur;
        mapMurs.put(mur, new Point(3,2));
        
        // F3
        mur = new Mur(this, "fin", 4);
        grilleMurs[5][2] = mur;
        mapMurs.put(mur, new Point(5,2));
        
        // G3
        mur = new Mur(this, "droit", 2);
        grilleMurs[6][2] = mur;
        mapMurs.put(mur, new Point(6,2));
        
        // H3
        mur = new Mur(this, "fin", 2);
        grilleMurs[7][2] = mur;
        mapMurs.put(mur, new Point(7,2));
        
        // C5
        mur = new Mur(this, "fin", 4);
        grilleMurs[2][4] = mur;
        mapMurs.put(mur, new Point(2,4));
        
        // D5
        mur = new Mur(this, "fin", 2);
        grilleMurs[3][4] = mur;
        mapMurs.put(mur, new Point(3,4));
        
        // Q3
        mur = new Mur(this, "fin", 4);
        grilleMurs[16][2] = mur;
        mapMurs.put(mur, new Point(16,2));
        
        // R3
        mur = new Mur(this, "fin", 2);
        grilleMurs[17][2] = mur;
        mapMurs.put(mur, new Point(17,2));
        
        // M3
        mur = new Mur(this, "fin", 4);
        grilleMurs[12][2] = mur;
        mapMurs.put(mur, new Point(14,2));
        
        // N3
        mur = new Mur(this, "droit", 2);
        grilleMurs[13][2] = mur;
        mapMurs.put(mur, new Point(15,2));
        
        // O3
        mur = new Mur(this, "fin", 2);
        grilleMurs[14][2] = mur;
        mapMurs.put(mur, new Point(16,2));
        
        // Q5
        mur = new Mur(this, "fin", 4);
        grilleMurs[16][4] = mur;
        mapMurs.put(mur, new Point(16,4));
        
        // R5
        mur = new Mur(this, "fin", 2);
        grilleMurs[17][4] = mur;
        mapMurs.put(mur, new Point(17,4));
        
    }
    
    /** Permet a une entité  de percevoir sont environnement proche et de définir sa strétégie de déplacement 
     * (fonctionalité utilisée dans choixDirection() de Fantôme)
     */
    public Object regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }

    
    /** Si le déclacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        
        boolean retour;
        Point pCourant = map.get(e);
        Point pCible = calculerPointCible(pCourant, d);
        if (contenuDansGrille(pCible) && objetALaPosition(pCible) == null) { // a adapter (collisions murs, etc.)
            deplacerEntite(pCourant, pCible, e);
            retour = true;
        } else {
            retour = false;
        }
        return retour;
    }
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;   
        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
        }
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    /** Vérifie que p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Object objetALaPosition(Point p) {
        Object retour = null;
        if (contenuDansGrille(p)) {
            if (grilleMurs[p.x][p.y] != null)retour = grilleMurs[p.x][p.y];
            else retour = grilleEntites[p.x][p.y];
        }
        return retour;
    }
    
    /**
     * Un processus est créé et lancé, celui-ci execute la fonction run()
     */
    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            for (Entite e : map.keySet()) { // déclenchement de l'activité des entités, map.keySet() correspond à la liste des entités
                e.run(); 
            }
            setChanged();
            notifyObservers(); // notification de l'observer pour le raffraichisssement graphique
            try {
                Thread.sleep(500); // pause de 0.5s
            } catch (InterruptedException ex) {
                Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
